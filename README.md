# CleanroomDevEnv

Kappa-Uniminedを採用した最後のCleanroomModTemplateとSumire LabsのModTemplateをベースに改造したCleanroom Mod開発テンプレート
Kappa-UniminedではなくCleanroom Gradle、そしてバージョン管理にはCleanroom Versioningを使用しており、JDK25 / Cleanroom 0.7.x で動作します。

Renovate Appで、毎週月曜0:00〜6:00（日本時間）に依存関係の更新を1つのPRへまとめます。

利用前に[Renovateの導入手順](.github/RENOVATE.md)に従って公式Appを有効化してください。トークンの作成は不要です。

## リリースの変更履歴

タグを付けるコミットのルートに `CHANGELOG.md` を置き、バージョンごとの見出しを作ります。
たとえばタグ `v1.2.3` なら `## [1.2.3] - 2026-11-07` の下に本文を書きます。
プレリリースは `## [1.2.3-beta]` のように接尾辞までタグと揃えます。
次のレベル1・2見出しまでの本文を、GitHub Releases・Modrinth・CurseForgeで共有します。
ファイルがない場合はGitHubの自動生成を使います。ファイルがあり、該当する本文がない場合は公開前に停止します。

## ライセンス

このテンプレートは[MIT License](LICENSE.md)です。
