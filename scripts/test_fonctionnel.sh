#!/usr/bin/env bash
# vide la base attentino
cd "$(dirname "$0")/.."

BASE="http://localhost:8080"
TMP=$(mktemp -d)
trap 'rm -rf "$TMP"' EXIT
PASS=0
FAIL=0

if [ "$1" != "-y" ]; then
    read -r -p "ce script vide la base de donnees, continuer ? [o/N] " rep
    [ "$rep" = "o" ] || [ "$rep" = "O" ] || exit 1
fi

if ! curl -s -o /dev/null "$BASE"; then
    echo "l'app ne repond pas sur $BASE, lancez docker compose up -d"
    exit 1
fi

./scripts/reset_setup.sh --tout > /dev/null

# tech img en code
printf '\x89PNG\r\n\x1a\n\x00\x00\x00\rIHDR\x00\x00\x00\x01\x00\x00\x00\x01\x08\x02\x00\x00\x00\x90wS\xde\x00\x00\x00\x0cIDATx\x9cc\xf8\xcf\xc0\x00\x00\x00\x03\x00\x01\x9d\xc0\xf6\x11\x00\x00\x00\x00IEND\xaeB\x60\x82' > "$TMP/img.png"

check() {
    local nom="$1" attendu="$2" obtenu="$3"
    if [ "$obtenu" = "$attendu" ]; then
        PASS=$((PASS+1)); echo "  ok    $nom"
    else
        FAIL=$((FAIL+1)); echo "  ECHEC $nom (attendu: $attendu, obtenu: $obtenu)"
    fi
}

contient() {
    local nom="$1" url="$2" texte="$3"
    if curl -s "$url" | grep -q "$texte"; then
        PASS=$((PASS+1)); echo "  ok    $nom"
    else
        FAIL=$((FAIL+1)); echo "  ECHEC $nom ($texte introuvable sur $url)"
    fi
}

echo "-- setup"
check "redirection vers /setup" "302:$BASE/setup" \
    "$(curl -s -o /dev/null -w '%{http_code}:%{redirect_url}' "$BASE/")"
check "mdp faible refuse" "200" \
    "$(curl -s -o /dev/null -w '%{http_code}' -X POST "$BASE/setup" \
        -F nom=TestResto -F url="$BASE" -F 'couleur=#b45309' -F motDePasse=faible \
        -F adresse='1 rue du Test' -F banniere=@"$TMP/img.png")"
check "setup valide" "302:$BASE/" \
    "$(curl -s -o /dev/null -w '%{http_code}:%{redirect_url}' -X POST "$BASE/setup" \
        -F nom=TestResto -F url="$BASE" -F 'couleur=#b45309' -F motDePasse=Password1 \
        -F adresse='1 rue du Test' -F telephone='01 02 03 04 05' -F email=t@t.fr \
        -F horaires='12h-14h' -F description='Resto de test' -F banniere=@"$TMP/img.png")"
check "/setup inaccessible une fois configure" "302:$BASE/" \
    "$(curl -s -o /dev/null -w '%{http_code}:%{redirect_url}' "$BASE/setup")"
contient "nom dans la page" "$BASE/" "TestResto"

echo "-- admin"
CK="$TMP/cookies.txt"
check "/admin protege" "302:$BASE/admin/login" \
    "$(curl -s -o /dev/null -w '%{http_code}:%{redirect_url}' "$BASE/admin")"
check "mauvais mdp refuse" "200" \
    "$(curl -s -o /dev/null -w '%{http_code}' -X POST "$BASE/admin/login" -d motDePasse=mauvais)"
check "login" "302" \
    "$(curl -s -c "$CK" -o /dev/null -w '%{http_code}' -X POST "$BASE/admin/login" -d motDePasse=Password1)"

echo "-- crud"
check "creation section carte" "302" \
    "$(curl -s -b "$CK" -o /dev/null -w '%{http_code}' -X POST "$BASE/admin/carte" -d 'nom=Entrees&position=1')"
SECTION_ID=$(docker compose exec -T db mysql -urestaurant -prestaurant my_restaurant -N -e "SELECT MAX(id) FROM carte_section;" 2>/dev/null)
check "edition section carte" "302" \
    "$(curl -s -b "$CK" -o /dev/null -w '%{http_code}' -X POST "$BASE/admin/carte/$SECTION_ID" -d 'nom=Nos entrees&position=1')"
check "creation menu avec prix custom" "302" \
    "$(curl -s -b "$CK" -o /dev/null -w '%{http_code}' -X POST "$BASE/admin/menus" -d 'nom=Menu test&prix=15.00')"
MENU_ID=$(docker compose exec -T db mysql -urestaurant -prestaurant my_restaurant -N -e "SELECT MAX(id) FROM menus;" 2>/dev/null)
check "creation section de menu" "302" \
    "$(curl -s -b "$CK" -o /dev/null -w '%{http_code}' -X POST "$BASE/admin/menus/$MENU_ID/sections" -d 'nom=Plat&position=1')"
MSECTION_ID=$(docker compose exec -T db mysql -urestaurant -prestaurant my_restaurant -N -e "SELECT MAX(id) FROM menu_section;" 2>/dev/null)
check "creation plat" "302" \
    "$(curl -s -b "$CK" -o /dev/null -w '%{http_code}' -X POST "$BASE/admin/plats/save" \
        -F 'nom=Plat test' -F 'description=Un plat' -F prix=20.00 -F allergenes=GLUTEN \
        -F carteSectionId="$SECTION_ID" -F menuSectionIds="$MSECTION_ID" -F photo=@"$TMP/img.png")"
contient "plat sur la carte" "$BASE/carte" "Plat test"
contient "allergene affiche" "$BASE/carte" "gluten"
contient "section editee affichee" "$BASE/carte" "Nos entrees"

echo "-- prix des menus"
contient "prix custom affiche" "$BASE/menus" "15,00 €"
contient "prix defaut barre" "$BASE/menus" "line-through"
contient "vrai prix du plat barre" "$BASE/menus" "20,00 €"

echo "-- reviews"
check "creation review" "302" \
    "$(curl -s -o /dev/null -w '%{http_code}' -X POST "$BASE/reviews" \
        -d "nom=Testeur&date=$(date +%F)&note=5&commentaire=Tres bien")"
contient "review sur l'accueil" "$BASE/" "Testeur"
contient "etoiles affichees" "$BASE/reviews" "★"

echo "-- suppressions"
PLAT_ID=$(docker compose exec -T db mysql -urestaurant -prestaurant my_restaurant -N -e "SELECT MAX(id) FROM plat;" 2>/dev/null)
check "suppression plat" "302" \
    "$(curl -s -b "$CK" -o /dev/null -w '%{http_code}' -X POST "$BASE/admin/plats/$PLAT_ID/delete")"
check "suppression menu" "302" \
    "$(curl -s -b "$CK" -o /dev/null -w '%{http_code}' -X POST "$BASE/admin/menus/$MENU_ID/delete")"
check "suppression section carte" "302" \
    "$(curl -s -b "$CK" -o /dev/null -w '%{http_code}' -X POST "$BASE/admin/carte/$SECTION_ID/delete")"
check "pages publiques ok apres suppressions" "200 200 200 200" \
    "$(for p in / /carte /menus /reviews; do curl -s -o /dev/null -w '%{http_code} ' "$BASE$p"; done | sed 's/ $//')"
check "logout" "302" \
    "$(curl -s -b "$CK" -o /dev/null -w '%{http_code}' -X POST "$BASE/admin/logout")"

echo ""
echo "resultat : $PASS ok, $FAIL echec(s)"
[ "$FAIL" -eq 0 ]
