# Applitools Example: UFT Windows Desktop (stdwin) in Java

This is an example project for running tests using the UFT Developer SDK and the Applitools Eyes Images SDK.
It shows how to start automating visual tests for Windows Desktop applications, with the [UFT stdwin SDK/framework](https://admhelp.microfocus.com/uftdev/en/2021-23.4/JavaSDKReference/Content/JavaSDKReference/com/hp/lft/sdk/stdwin/package-summary.html) and the [Applitools Eyes Images SDK](https://applitools.com/docs/api-ref/category/images-java-class-index).

It uses:

* [Java](https://www.java.com/) as the programming language
* [UFT Developer Java SDK](https://admhelp.microfocus.com/uftdev/en/2021-23.4/JavaSDKReference/Content/JavaSDKReference/overview-summary.html) for simulating interactions with Windows desktop applications, and capturing visual snapshots.
* [Apache Maven](https://maven.apache.org/index.html) for dependency management
* [Applitools Eyes](https://applitools.com/platform/eyes/) for visual testing

To run this example project, you'll need:

1. **UFT Developer (installed and activated), and the jar files for the UFT Java SDK, which can be installed as described in the [UFT Developer Java Prerequisites](https://admhelp.microfocus.com/uftdev/en/2021-23.4/HelpCenter/Content/HowTo/prerequ-maven.htm). (For an existing UFT Developer Java project, these jar files are likely already being referenced. For new projects, the jar files can be added as "External Jars" in Eclipse, or similar in other IDEs.)**
2. An [Applitools account](https://auth.applitools.com/users/register), which you can register for free.
3. The [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/downloads/), version 8 or higher.
4. A good Java editor, such as [JetBrains IntelliJ IDEA](https://www.jetbrains.com/idea/).
5. [Apache Maven](https://maven.apache.org/download.cgi) (typically bundled with IDEs).

The example test cases are in [`UftDemo.java`](src/test/java/demo/UftDemo.java). The example test cases use the `com.hp.lft.sdk.stdwin` package, but can be updated to use other Windows Desktop related packages found in the UFT Developer Java SDK. Visual checkpoints are captured by using the UFT `getSnapshot()` method, converting its result from `RenderedImage` to a byte array, and sending the `byte` array of image data to Applitools Eyes with the Eyes Images SDK.


To execute tests, set the `APPLITOOLS_API_KEY` environment variable
to your [account's API key](https://applitools.com/tutorials/guides/getting-started/registering-an-account).