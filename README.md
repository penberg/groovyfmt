# `groovyfmt`

`groovyfmt` is a tool to automatically format your [Groovy](http://groovy-lang.org/) and [Jenkins pipeline](https://jenkins.io/doc/book/pipeline/) source code.

## Status

The tool is able to parse and format many Groovy source files already.
However, the tool still has known bugs, so use with extreme caution when formatting your code.
Help with testing and development is more than welcome!

## Getting Started

You can build the tool with:

```
mvn package
```

and then format a source code file with:

```
./target/groovyfmt <filename>
```
