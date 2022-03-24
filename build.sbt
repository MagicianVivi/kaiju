lazy val kaiju = (project in file("."))
  .settings(
    organization := "org.world.domination",
    name := "kaiju",
    scalaVersion := "2.13.8",
    scalacOptions ++= Seq(
      "-target:11",
      "-language:higherKinds",
      "-language:postfixOps",
      "-encoding",
      "utf-8", // Specify character encoding used by source files.
      "-explaintypes", // Explain type errors in more detail.
      "-feature", // Emit warning and location for usages of features that should be imported explicitly.
      "-unchecked", // Enable additional warnings where generated code depends on assumptions.
      "-Xcheckinit", // Wrap field accessors to throw an exception on uninitialized access.
      "-Xlint:adapted-args", // Warn if an argument list is modified to match the receiver.
      "-Xlint:constant", // Evaluation of a constant arithmetic expression results in an error.
      "-Xlint:delayedinit-select", // Selecting member of DelayedInit.
      "-Xlint:doc-detached", // A Scaladoc comment appears to be detached from its element.
      "-Xlint:inaccessible", // Warn about inaccessible types in method signatures.
      "-Xlint:infer-any", // Warn when a type argument is inferred to be `Any`.
      "-Xlint:missing-interpolator", // A string literal appears to be missing an interpolator id.
      "-Xlint:nullary-unit", // Warn when nullary methods return Unit.
      "-Xlint:option-implicit", // Option.apply used implicit view.
      "-Xlint:package-object-classes", // Class or object defined in package object.
      "-Xlint:poly-implicit-overload", // Parameterized overloaded implicit methods are not visible as view bounds.
      "-Xlint:private-shadow", // A private field (or class parameter) shadows a superclass field.
      "-Xlint:stars-align", // Pattern sequence wildcard must align with sequence component.
      "-Xlint:type-parameter-shadow", // A local type parameter shadows a type already in scope.
      "-Xlint:adapted-args", // Do not adapt an argument list (either by inserting () or creating a tuple) to match the receiver.
      "-Xlint:inaccessible", // Warn about inaccessible types in method signatures.
      "-Xlint:infer-any", // Warn when a type argument is inferred to be `Any`.
      "-Xlint:deprecation", // Warn of deprecated features
      "-Xlint:implicit-not-found", // Check @implicitNotFound and @implicitAmbiguous messages.
      "-Xlint:valpattern", // Enable pattern checks in val definitions.
      "-Xlint:eta-zero", // Warn on eta-expansion (rather than auto-application) of zero-ary method.
      "-Werror", // Fail the compilation if there are any warnings.
      "-Wdead-code", // Warn when dead code is identified.
      "-Wextra-implicit", // Warn when more than one implicit parameter section is defined.
      "-Wunused:imports", // Warn if an import selector is not referenced.
      "-Wunused:locals", // Warn if a local definition is unused.
      "-Wunused:privates", // Warn if a private member is unused.
      "-Wvalue-discard" // Warn when non-Unit expression results are unused.
    ),
    Compile / console / scalacOptions --= Seq("-Wunused:imports", "-Werror"),
    resolvers += Resolver.sonatypeRepo("releases"),
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-effect" % "3.3.8",
    )
  )
