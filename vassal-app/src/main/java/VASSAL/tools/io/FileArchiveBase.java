/*
 * Copyright (c) 2021 by Joel Uckelman
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
package VASSAL.tools.io;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public abstract class FileArchiveBase implements FileArchive {
  /**
   * Adds a file to the archive.
   *
   * @param path the internal path of the file to be added
   * @param extPath the external path of the file to be added
   * @throws IOException oops
   */
  @Override
  public void add(String path, String extPath) throws IOException {
    add(path, new File(extPath));
  }

  /**
   * Adds a file to the archive.
   *
   * @param path the internal path of the file to be added
   * @param extPath the external path to the file to be added
   * @throws IOException oops
   */
  @Override
  public void add(String path, File extPath) throws IOException {
    try (InputStream in = Files.newInputStream(extPath.toPath())) {
      add(path, in);
    }
  }

  /**
   * Adds the contents of a byte array to the archive.
   *
   * @param path the internal path of the file to be added
   * @param bytes the bytes to be added
   * @throws IOException oops
   */
  @Override
  public void add(String path, byte[] bytes) throws IOException {
    add(path, new ByteArrayInputStream(bytes));
  }

  /**
   * Adds the contents of an {@link InputStream} to the archive.
   *
   * @param path the internal path of the file to be added
   * @param in the <code>InputStream</code> to read from
   * @throws IOException oops
   */
  @Override
  public void add(String path, InputStream in) throws IOException {
    try (OutputStream out = getOutputStream(path)) {
      in.transferTo(out);
    }
  }
}
