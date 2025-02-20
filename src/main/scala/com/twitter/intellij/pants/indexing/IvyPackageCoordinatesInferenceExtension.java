package com.twitter.intellij.pants.indexing;

import java.util.List;

import com.intellij.indexing.shared.java.maven.MavenPackageCoordinatesInferenceExtension;
import com.intellij.indexing.shared.java.maven.MavenPackageId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class IvyPackageCoordinatesInferenceExtension implements MavenPackageCoordinatesInferenceExtension {

  @Nullable
  @Override
  public MavenPackageId infer(@NotNull String path, @NotNull List<String> reversePathElements) {
    Stream<String> elements = reversePathElements.stream();
    if (elements.noneMatch(s -> s.equals(".ivy2"))) return null;
    String[] fileName = elements.findFirst().map(
            s -> s.replace(".jar", "").split("-")
    ).orElse(null);
    if (fileName == null) return null;
    if (reversePathElements.size() < 4) return null;
    String groupId = reversePathElements.get(3);
    String artifactId = reversePathElements.get(2);
    String version = fileName[fileName.length - 1];
    if (!Pattern.matches("^\\d+\\.\\d+\\.\\d+", version)) return null;
    return new MavenPackageId(groupId, artifactId, version);
  }


}