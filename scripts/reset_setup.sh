#!/usr/bin/env bash
# flag pour tt : --tout
set -e
cd "$(dirname "$0")/.."

MYSQL="docker compose exec -T db mysql -urestaurant -prestaurant my_restaurant"

if [ "$1" = "--tout" ]; then
    echo "delete config + contenu"
    $MYSQL 2>/dev/null <<'SQL'
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE plat_menu_sections;
TRUNCATE TABLE plat_allergenes;
TRUNCATE TABLE plat;
TRUNCATE TABLE menu_section;
TRUNCATE TABLE menus;
TRUNCATE TABLE carte_section;
TRUNCATE TABLE review;
TRUNCATE TABLE restaurant_config;
SET FOREIGN_KEY_CHECKS = 1;
SQL
else
    echo "delete config"
    echo "delete contenu"
    $MYSQL -e "DELETE FROM restaurant_config;" 2>/dev/null
fi

echo "réinitialisé"
