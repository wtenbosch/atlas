package nl.overheid.aerius.templates;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

import nl.overheid.aerius.configuration.AtlasServices;

public class TextTemplates {
  private static final Logger LOG = LoggerFactory.getLogger(TextTemplates.class);

  private final AtlasServices cfg;

  public TextTemplates(final AtlasServices cfg) {
    this.cfg = cfg;
  }

  public void init() throws IOException {
    final String base = cfg.getTextTemplates();

    final MutableDataSet options = new MutableDataSet();

    final Parser parser = Parser.builder(options).build();
    final HtmlRenderer renderer = HtmlRenderer.builder()
        .attributeProviderFactory(BlankAnchorAttributeProvider.factory())
        .build();

    final List<File> resourceFiles = getResourceFiles(base);

    LOG.info("Indexing:" + resourceFiles.size());

    resourceFiles.forEach(v -> {
      try {
        final Node document = parser.parseReader(new InputStreamReader(new FileInputStream(v)));
        final String html = renderer.render(document);

        final String fileName = v.getAbsoluteFile().getCanonicalPath();
        final String cleanedFileName = Stream.of(fileName.split(File.separator))
            .map(part -> part.indexOf("_") > -1 ? part.substring(part.indexOf("_") + 1) : part)
            .collect(Collectors.joining(File.separator));

        String key = cleanedFileName.substring(cleanedFileName.lastIndexOf(base) + base.length()).replace(File.separator, "/");
        if (key.startsWith("/")) {
          key = key.substring(1);
        }

        LOG.info("Interpreted and filed [{}] as html text", key);

        TextCache.addText(key, html);
      } catch (final Exception e) {
        LOG.error("Could not interpret file: {}", v);
      }
    });
  }

  private List<File> getResourceFiles(final String base) throws IOException {
    LOG.info("Looking for and indexing all markdown texts in: {}", base);

    return scavenge(new File(base)).collect(Collectors.toList());
  }

  private Stream<File> scavenge(final File file) {
    if (file.isDirectory()) {
      LOG.info("Moving into: {}", file.toString());
      return Stream.of(file.listFiles()).flatMap(v -> scavenge(v));
    } else if (file.isFile() && file.getName().endsWith(".md")) {
      LOG.info("Indexing: {}", file);
      return Stream.of(file);
    } else {
      return Stream.of();
    }
  }
}
