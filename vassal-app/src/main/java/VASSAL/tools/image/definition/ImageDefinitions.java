package VASSAL.tools.image.definition;

import VASSAL.tools.imageop.SourceOp;
import VASSAL.tools.imageop.SourceOpBitmapImpl;
import VASSAL.tools.imageop.SourceOpSVGImpl;
import VASSAL.tools.IteratorUtils;

import java.io.InputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

public class ImageDefinitions {

  private final JsonNode root;

  private final Map<String, JsonNode> defs = new HashMap<>();

  public ImageDefinitions() {
    root = new ArrayNode(new JsonNodeFactory(false));
  }

  public ImageDefinitions(InputStream in) throws IOException {
    root = new ObjectMapper().readTree(in);

    for (final JsonNode i: IteratorUtils.iterate(root.elements())) {
      defs.put(i.get("id").toString(), i);
    }
  }

  public SourceOp get(String id) {

    final JsonNode i = defs.get(id);
    if (i != null) {
      // final String i.get("id").asText();
      
      final String src = i.get("src").asText()
      final SourceOp sop = src.endsWith(".svg") ? 
        new SourceOpSVGImpl(src) : new SourceOpBitmapImpl(src);

      if (i.size() == 2) {
        // this is a definition for a whole image
        return sop;
      }
      else if (i.size() == 6) {
        // this is a definition for a crop

        final int x = i.get("x").asInt();
        final int y = i.get("y").asInt();
        final int w = i.get("width").asInt();
        final int h = i.get("height").asInt();

        return new CropOpBitmapImpl(sop, x, y, x + w, y + h);
      }
    }
    else {
      // split the id to check if this is a grid cell 
    
      final int div = id.indexOf('@');
      if (div != -1) {
        final int comma = id.indexof(',');
        if (comma != -1) {
          final String grid = id.substring(0, div);
          final int row = Integer.valueOf(id.substring(div + 1, comma));
          final int col = Integer.valueOf(id.substring(comma + 1));
         
          final int x = i.get("x").asInt();
          final int y = i.get("y").asInt();
          final int w = i.get("width").asInt();
          final int h = i.get("height").asInt();



          return new CropOpBitmapImpl(sop, );
        }
      }


      // this is a definition for a grid




      if (id.endsWith(".svg")) { //NON-NLS
        return new SourceOpSVGImpl(id);
      }
      else {
        return new SourceOpBitmapImpl(id);
      }
    }
  }
}
