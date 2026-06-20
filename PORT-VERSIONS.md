# Verified 1.21.1 NeoForge dependency versions (researched 2026-06-17)

Paste-ready for `gradle.properties`:

```properties
# Platform
minecraft_version=1.21.1
neoforge_version=21.1.233
parchment_mc_version=1.21.1
parchment_date=2024.11.17           # org.parchmentmc.data:parchment-1.21.1

# Architectury
architectury_version=13.0.8         # dev.architectury:architectury-neoforge
# KEEP plugin/loom versions from current 1.20.1 build (they support 1.21.1 — DO NOT downgrade):
#   architectury-plugin    3.4-SNAPSHOT
#   dev.architectury.loom  1.10-SNAPSHOT  (research said 1.7; that's WRONG — loom is forward-compatible
#                                          and the live 1.20.1 branch already uses 1.10-SNAPSHOT)

# Mods
gecko_lib_version=4.7.7             # software.bernie.geckolib:geckolib-neoforge-1.21.1
terra_blender_version=4.1.0.8       # com.github.glitchfiend:TerraBlender-neoforge:1.21.1-4.1.0.8
more_hitboxes_modrinth_id=1Cu922wS  # maven.modrinth:more-hitboxes:1Cu922wS (neoforge 1.21.1-1.9.4-alpha)
```

## Notes / gotchas
- **MixinExtras is BUNDLED by NeoForge 21.1.x** — do NOT shade/jar-in-jar it (the forge build
  did `include(...mixinextras-forge:0.4.1)`). On NeoForge: at most `compileOnly`/`annotationProcessor`
  `io.github.llamalad7:mixinextras-neoforge:0.5.4`, never at runtime.
- **MoreHitboxes**: pull via Modrinth maven (`https://api.modrinth.com/maven`) using version ID
  `1Cu922wS`. The DarkPred Cloudsmith maven coordinate could not be verified.
- **TerraBlender** maven `https://maven.glitchfiend.com` was flaky during research; `modmaven.dev`
  or cursemaven are fallbacks. Coordinate: `com.github.glitchfiend:TerraBlender-neoforge:1.21.1-4.1.0.8`.
- Keep Architectury **plugin/loom** at the current `3.4-SNAPSHOT` / `1.10-SNAPSHOT` (they support
  1.21.1). The research agent's "use loom 1.7" was incorrect.
