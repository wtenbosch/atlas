package nl.overheid.aerius.templates;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.machinezoo.noexception.Exceptions;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

import templates.TextTemplateBase;

public class TextTemplates {
  private static final Logger LOG = LoggerFactory.getLogger(TextTemplates.class);

  public void init() throws IOException {
    final String base = TextTemplateBase.class.getPackage().toString().split(" ")[1].replace(".", File.separator);

    final MutableDataSet options = new MutableDataSet();

    final Parser parser = Parser.builder(options).build();
    final HtmlRenderer renderer = HtmlRenderer.builder()
        .attributeProviderFactory(BlankAnchorAttributeProvider.factory())
        .build();

    final List<File> resourceFiles = getResourceFiles(base);
    resourceFiles.forEach(Exceptions.sneak().consumer(v -> {
      final Node document = parser.parseReader(new InputStreamReader(new FileInputStream(v)));
      final String html = renderer.render(document);

      final String fileName = v.getAbsoluteFile().getCanonicalPath();
      final String cleanedFileName = Stream.of(fileName.split(File.separator))
          .map(part -> part.indexOf("_") > -1 ? part.substring(part.indexOf("_") + 1) : part)
          .collect(Collectors.joining(File.separator));

      final String key = cleanedFileName.substring(cleanedFileName.lastIndexOf(base) + base.length() + 1).replace(File.separator, "/");

      LOG.debug("Interpreted and filed [{}] as html text", key);

      TextCache.addText(key, html);
    }));
  }

  private List<File> getResourceFiles(final String base) throws IOException {
    LOG.info("Looking for and indexing all markdown texts in: {}", base);

    final ClassLoader loader = getContextClassLoader();
    final URL url = loader.getResource(base);
    final String path = url.getPath();
    return scavenge(new File(path)).collect(Collectors.toList());
  }

  private Stream<File> scavenge(final File file) {
    if (file.isDirectory()) {
      return Stream.of(file.listFiles()).flatMap(v -> scavenge(v));
    } else if (file.isFile() && file.getName().endsWith(".md")) {
      return Stream.of(file);
    } else {
      return Stream.of();
    }
  }

  private ClassLoader getContextClassLoader() {
    return Thread.currentThread().getContextClassLoader();
  }
}
