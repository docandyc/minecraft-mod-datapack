---
name: mc-datapack
description: Use when creating, editing, explaining, or debugging Minecraft Java datapacks in this project, especially pack structure, functions, tags, recipes, loot tables, advancements, predicates, scoreboards, and version-specific pack_format rules.
---

# Minecraft Datapack

Use this project-local skill for Minecraft Java datapack work under this repository.

## Workflow

- Confirm the target Minecraft Java version before choosing version-specific values.
- For Minecraft Java 1.20.1, use `pack_format: 15`.
- Keep datapack roots self-contained, with `pack.mcmeta` at the datapack root.
- Put namespaced content under `data/<namespace>/`.
- Put functions under `data/<namespace>/functions/`.
- Use `data/minecraft/tags/functions/load.json` for functions that run after `/reload`.
- Use `data/minecraft/tags/functions/tick.json` for functions that run every tick.
- Prefer small `.mcfunction` files organized by feature folders such as `skills/`, `systems/`, or `events/`.
- Keep namespaces lowercase and avoid spaces.

## Validation

- Check JSON files for valid JSON syntax.
- Check `.mcfunction` command names and resource locations for lowercase namespaced IDs.
- Test in game with `/reload`, `/datapack list`, and targeted `/function <namespace>:<path>` calls.

