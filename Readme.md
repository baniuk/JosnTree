# JsonTree

This simple tool displays Json files in form of hierarchical Tree. Text output and GUI are supported. See examples below:

```text
+com.google.gson.internal.LinkedTreeMap (root)
|--java.lang.String (className)
|--+com.google.gson.internal.LinkedTreeMap (timeStamp)
|--|--java.lang.String (version)
|--|--java.lang.String (buildstamp)
|--|--java.lang.String (name)
|--java.lang.String (createdOn)
|--+com.google.gson.internal.LinkedTreeMap (obj)
|--|--+com.google.gson.internal.LinkedTreeMap (BOAState)
|--|--|--+com.google.gson.internal.LinkedTreeMap (boap)
|--|--|--|--+com.google.gson.internal.LinkedTreeMap (orgFile)
|--|--|--|--|--java.lang.String (path)
|--|--|--|--+com.google.gson.internal.LinkedTreeMap (outputFileCore)
|--|--|--|--|--java.lang.String (path)
|--|--|--|--java.lang.String (fileName)
|--|--|--|--java.lang.Double (NMAX)
|--|--|--|--java.lang.Double (delta_t)
|--|--|--|--java.lang.Double (sensitivity)
|--|--|--|--java.lang.Double (f_friction)
|--|--|--|--java.lang.Double (FRAMES)
|--|--|--|--java.lang.Double (WIDTH)
|--|--|--|--java.lang.Double (HEIGHT)
|--|--|--|--java.lang.Double (cut_every)
|--|--|--|--java.lang.Boolean (oldFormat)
|--|--|--|--java.lang.Boolean (saveSnake)
```

![gui](img/gui.png)

## Usage

```
> java -jar .\target\JsonTree-1.0.0-SNAPSHOT.jar

Parsing failed. Reason: Missing required option: f
usage: JsonTree -f <FILE> [-gui] [-help]
 -f <FILE>   JSon file to open
 -gui        Show GUI in place of stdout
 -help       print this message
```

## Build

```bash
mvn package                     # produces clear jar
mvn site                        # build project site
mvn package -P deploy           # produces executabe jar with dependencies
mvn package -P dev-collectdeps  # extracts dependencies
```
