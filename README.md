# CoqingTemplate

<!--toc:start-->
- [CoqingTemplate](#coqingtemplate)
  - [Creating the plugin](#creating-the-plugin)
  - [Modifying Files](#modifying-files)
  - [paperweight-userdev](#paperweight-userdev)
  - [Utils Library](#utils-library)
<!--toc:end-->

---

This is the CoqingTemplate, which uses Gradle for its build system.
To use it, please follow these instructions:

## Creating the plugin

1. Click on "Use this template", then "Create a new repository".
2. Change the name of the repo, then click "Create".
3. Clone the repo locally by clicking on "Code",
then copy the URL and type in a terminal `git clone`
and paste in the URL you got.
4. Voila! You now have a new template! Now proceed to modifying the files.

## Modifying Files

To get started, please do the following:

1. Head to `settings.gradle.kts` and change the project name.
2. Head to `build.gradle.kts` and change the group, version and description at
the top of the file, and also change the information in `publishing`.
3. Change the folder name according to what you typed, and the main class's name
too.
4. Enjoy!

## paperweight-userdev

**More info: <https://docs.papermc.io/paper/dev/userdev/>**

From the PaperMC wiki:
> paperweight is the name of Paper’s custom build tooling.
The paperweight-userdev Gradle plugin part of that provides access
to internal code (also known as NMS) during development.

This template includes paperweight, which allows you to use internal NMS code.
Keep in mind that

## Utils Library

**Main repository: <https://github.com/Coqing31/CoqingUtils/>**

This template includes and allows you to use the CoqingUtils.
To use the library, refer to the README on the library's page.
