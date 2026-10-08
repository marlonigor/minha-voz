#!/usr/bin/env bash
set -euo pipefail

TAG="${1:-}"
if [ -z "$TAG" ]; then
    echo "Uso: $0 <tag-ou-versao> [caminho-changelog]" >&2
    exit 1
fi

VERSION="${TAG#v}"
CHANGELOG_FILE="${2:-CHANGELOG.md}"

if [ ! -f "$CHANGELOG_FILE" ]; then
    echo "Erro: Arquivo $CHANGELOG_FILE nao encontrado." >&2
    exit 1
fi

awk -v ver="$VERSION" '
    BEGIN { inside = 0 }
    $0 ~ "^## \\[" ver "\\]" { inside = 1; next }
    inside && /^## \[/ { inside = 0 }
    inside { print }
' "$CHANGELOG_FILE"
