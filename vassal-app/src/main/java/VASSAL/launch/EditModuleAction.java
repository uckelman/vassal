/*
 *
 * Copyright (c) 2000-2009 by Rodney Kinney, Brent Easton
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License (LGPL) as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, copies are available
 * at http://www.opensource.org.
 */

package VASSAL.launch;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.swing.JFrame;

import VASSAL.build.GameModule;
import VASSAL.i18n.Resources;
import VASSAL.tools.ArchiveWriter;
import VASSAL.tools.io.DirArchive;
//import VASSAL.tools.io.ZipArchive;
import VASSAL.tools.swing.SwingUtils;

public class EditModuleAction extends LoadModuleAction {
  private static final long serialVersionUID = 1L;

  public EditModuleAction(Component comp) {
    super(comp);
    putValue(NAME, Resources.getString("Main.edit_module"));
  }

  public EditModuleAction(File moduleFile) {
    super(moduleFile);
    putValue(NAME, Resources.getString("Main.edit_module"));
  }

  @Override
  protected void loadModule(File f) throws IOException {
//    GameModule.init(new GameModule(new ArchiveWriter(new ZipArchive(f), ".vmod"))); //NON-NLS
//    GameModule.init(new GameModule(new ArchiveWriter(new DirArchive(f.toPath().getParent()), ".vmod"))); //NON-NLS
    GameModule.init(new GameModule(new ArchiveWriter(new DirArchive(Path.of("/home/uckelman/projects/VASSAL/trunk/tmp")), ".vmod"))); //NON-NLS

// FIXME: really hide the MM?
//    ModuleManagerWindow.getInstance().setVisible(false);
    final JFrame frame = GameModule.getGameModule().getPlayerWindow();
    frame.setVisible(true);

    // GameModule only produces a final buildString() once its frame displays
    GameModule.getGameModule().updateLastSave();

    final ModuleEditorWindow w =
      new ModuleEditorWindow(GameModule.getGameModule());
    w.setLocation(0, frame.getY() + frame.getHeight());
    w.setSize(SwingUtils.getScreenBounds(frame).width / 2, w.getHeight());
    w.setVisible(true);
  }
}
