# CleanroomDevEnv

CleanroomGradleを使ったCleanroom向けのMod開発テンプレート
[Sumire-Labs/ModTemplate](https://github.com/Sumire-Labs/ModTemplate)がベースになっています(SumireLabs/ModTemplateはkappa-uniminedのCleanroomModTemplateがベースです。)

このリポジトリの依存更新にはRenovateを使っています。
テンプレートからModを作るときは、保守用の`.github/renovate.json`を削除してください。

## ブランチ

Javaの場合は`main`を使ってください。Mixin・Coremod・ATは`gradle.properties`から有効にできます
Kotlinは`kotlin`、Scalaは`scala`を使用してください

## 初期設定

1. JDK 25を用意してIntelliJのGradle JVMに設定します
2. `gradle.properties`の`mod_id`、`mod_name`、`root_package`を変更します
3. `src/main/java`と`src/test/java`のフォルダ名・package宣言・importをリネームします
4. Mixinを使う場合は`cg.userdev.refmap`の名前も変更します。ATを使う場合は、ATファイル名を`<mod_id>_at.cfg`に変更します

バージョンはCleanroom VersioningがGit履歴とタグから決めます。
Git履歴のないフォルダへコピーした場合は、初回コミットを作ってからビルドしてください。
現在のバージョンは`./gradlew -q printVersion`で確認できます。

## ビルドと起動

IntelliJでは、フォルダをGradleプロジェクトとして開いてください。
Gradleの`runClient`・`runServer`タスク、または生成される`2. Run Client`・`3. Run Server`から起動できます。

`<mod_id>-<version>.jar`が配布用、`*-mcp.jar`が開発用です。
`generate_sources_jar`・`generate_javadocs_jar`を有効にすると、ソース・JavadocのJARも作成します。

## Mod依存の追加

`gradle/scripts/dependencies.gradle.kts`の`dependencies`ブロックに追加してください。
配布用のSRG名JARには`cleanroom.deobf(...)`を使います。

```kotlin
dependencies {
    add("implementation", cleanroom.deobf("group:other-mod:version"))
    add("runtimeOnly", cleanroom.deobf("group:runtime-mod:version"))
    add("compileOnly", cleanroom.deobf(files("libs/other-mod.jar")))
}
```

MCP名の開発用JARや、Minecraftの名前に依存しないライブラリは、そのまま指定します。

```kotlin
dependencies {
    add("implementation", "group:other-mod:version:dev")
    add("implementation", "group:library:version")
}
```

追加のMavenリポジトリも同じファイルに書けます。

## ライブラリの同梱

配布JARにライブラリを含める場合は、`contain`か`shade`に追加します。
`contain`は依存JARをそのまま同梱し、Modのロード時に展開させます。
`shade`はShadowでライブラリのクラスを取り込みます。使う場合は先に`enable_shadow=true`を設定してください。

```kotlin
dependencies {
    add("contain", "group:embedded-library:version")
    add("shade", "group:shaded-library:version")
}
```

relocationなどの独自設定は`gradle/scripts/extra.gradle.kts`に書いてください。

## Reference

`src/main/java/com/example/modid/Reference.java`に、Mod ID・名前・バージョンの定数があります。
`ExampleMod`の`@Mod`注釈などで使っています。
`@{MOD_VERSION}`などの値は、コンパイル時にTokenEnvoyが置換します。
パッケージ名を変更したら、このクラスの配置と参照先も変更してください。

## Mixin

`gradle.properties`で`enable_mixin=true`にしてください。

- Mixinクラスは`src/main/java/<パッケージ>/mixin/`に置きます。
- `src/main/resources/modid.mixin.json`の`mixins`・`client`・`server`にクラス名を追加します。
- 条件付きで適用したい場合は、`ModMixinConfigPlugin`の`shouldApplyMixin`に処理を書きます。

設定ファイル名はビルド時に`<mod_id>.mixin.json`へ変わります。
refmapはCleanroomGradleが生成します。`cg.userdev.refmap`にはMod固有の名前を指定してください。
開発時に変換後のクラスを確認したい場合は、`enable_mixin_debug=true`にします。

## Coremod

`enable_coremod=true`にすると、`<root_package>.core.LoadingPlugin`が使われます。
サンプルの`LoadingPlugin`に、使用するTransformerなどを登録してください。
クラスを別の場所へ移す場合は、`coremod_plugin_class`に完全修飾名を指定できます。
`@Mod`クラスを含まないJARにする場合は、`coremod_includes_mod=false`にします。

## Access Transformer

`use_access_transformer=true`にすると、`src/main/resources/META-INF/<mod_id>_at.cfg`が使われます。
フィールド・メソッドはSRG名で書いてください。
サンプルにはMinecraftの`fileResourcepacks`をpublicにする行がコメントアウトされています。

```text
public net.minecraft.client.Minecraft field_130070_K
```

別のファイルを使う場合や複数指定する場合は、`access_transformers`にファイル名を空白区切りで指定します。

## Minecraftのソース

IntelliJへのGradle同期時に、CleanroomGradleがUserdevのソースを用意します。
MinecraftやForgeのクラスを開いてもソースが表示されない場合は、Gradleを再同期してください。

## Spotless

`enable_spotless=true`で、ビルド時にコードの整形をチェックします。
修正するときは`./gradlew spotlessApply`、チェックだけなら`./gradlew spotlessCheck`を使ってください。
設定ファイルは`gradle/spotless/`にあります。
一部を整形から外したい場合は、`// spotless:off`と`// spotless:on`で囲めます。

## GitHub Actions

`build.yml`はpush・PR・手動実行で動きます。
Ubuntuでビルド・JUnit・Spotlessを実行し、JARをActionsの成果物としてアップロードします。
テスト用のビルドを配る場合は、ここから取得できます。

`release.yml`はバージョンタグのpushで動き、GitHub Releaseを作成します。
手動実行する場合も、実行対象にタグを選んでください。
CF・MRのプロジェクトIDやMavenの公開先を設定しておくと、同じworkflowで公開します。

| 公開先 | gradle.properties | Actions Secrets |
| --- | --- | --- |
| CurseForge | `curseforge_project_id`（数値ID） | `CURSEFORGE_TOKEN` |
| Modrinth | `modrinth_project_id` | `MODRINTH_TOKEN` |
| Maven | `publish_to_maven=true`、`maven_name`、`maven_url` | `MAVEN_USERNAME`、`MAVEN_PASSWORD` |

Mavenへの公開をローカルで試す場合は、`publish_to_local_maven=true`にして`./gradlew publishToMavenLocal`を実行します。

## リリースの変更履歴

タグを付けるコミットのルートに`CHANGELOG.md`を置き、タグと同じバージョンの見出しを作ってください。
たとえば`0.1.0`タグなら、次のように書きます。

```markdown
## [0.1.0] - 2026-11-09

### Added
- 最初のリリース
```

この見出しの本文をGitHub Releases・CurseForge・Modrinthで共有します。
`CHANGELOG.md`がない場合はGitHubの自動生成を使います。
ファイルがあるのに該当バージョンの本文がない場合は、公開前に停止します。


## ライセンス

[MIT License](LICENSE.md)
