package nl.overheid.aerius.templates;

import com.vladsch.flexmark.ast.Link;
import com.vladsch.flexmark.html.AttributeProvider;
import com.vladsch.flexmark.html.AttributeProviderFactory;
import com.vladsch.flexmark.html.IndependentAttributeProviderFactory;
import com.vladsch.flexmark.html.renderer.AttributablePart;
import com.vladsch.flexmark.html.renderer.LinkResolverContext;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.html.Attributes;

public class BlankAnchorAttributeProvider implements AttributeProvider {
  @Override
  public void setAttributes(final Node node, final AttributablePart part, final Attributes attributes) {
    if (node instanceof Link && part == AttributablePart.LINK) {
      attributes.replaceValue("target", "_blank");
    }
  }

  public static AttributeProviderFactory factory() {
    return new IndependentAttributeProviderFactory() {

      @Override
      public AttributeProvider apply(final LinkResolverContext context) {
        return new BlankAnchorAttributeProvider();
      }
    };
  }
}
