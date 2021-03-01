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

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.FileVisitOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DirArchive extends FileArchiveBase {

  private final Path dir;

  public DirArchive(Path dir) {
    this.dir = dir;
  }

  /**
   * Gets the path to the archive file.
   *
   * @return the path as a <code>String</code>
   */
  @Override
  public String getName() {
    return dir.toString();
  }

  /**
   * Gets the path to the archive file.
   *
   * @return the path as a <code>File</code>
   */
  @Override
  public File getFile() {
    return dir.toFile();
  }

  /**
   * Gets an {@link InputStream} to read from the given file.
   *
   * @param path the path to the file in the archive
   * @return an <code>InputStream</code> containing the requested file
   * @throws IOException oops
   */
  @Override
  public InputStream getInputStream(String path) throws IOException {
    return Files.newInputStream(dir.resolve(path));
  }

  /**
   * Gets an {@link OutputStream} to write to the given file.
   *
   * @param path the path to the file in the archive
   * @return an <code>OutputStream</code> for the requested file
   * @throws IOException oops
   */
  @Override
  public OutputStream getOutputStream(String path) throws IOException {
    return Files.newOutputStream(dir.resolve(path));
  }

  /**
   * Removes a file from the archive.
   *
   * @param path the path to the file in the archive
   * @return <code>true</code> if the file existed in the archive
   * @throws IOException oops
   */
  @Override
  public boolean remove(String path) throws IOException {
    return Files.deleteIfExists(dir.resolve(path));
  }

  /**
   * Reverts the archive to its last saved state.
   *
   * @throws IOException oops
   */
  @Override
  public void revert() throws IOException {
  }

  /**
   * Forces all changes to the archive to disk.
   *
   * @throws IOException oops
   */
  @Override
  public void flush() throws IOException {
  }

  /**
   * Closes the archive, writing all changes to disk.
   *
   * @throws IOException oops
   */
  @Override
  public void close() throws IOException {
  }

  /**
   * Queries whether a file exists in the archive.
   *
   * @param path the path to the file in the archive
   * @return <code>true</code> if the file exists in the archive
   * @throws IOException oops
   */
  @Override
  public boolean contains(String path) throws IOException {
    return Files.exists(dir.resolve(path));
  }

  /**
   * Queries whether the archive is closed.
   *
   * @return <code>true</code> if the archive is closed
   */
  @Override
  public boolean isClosed() {
    return false;
  }

  /**
   * Queries whether the archive has unsaved modifications.
   *
   * @return <code>true</code> if the archive is modified
   */
  @Override
  public boolean isModified() {
    return false;
  }

  /**
   * Gets the size of a file in the archive, in bytes.
   *
   * @param path the path to the file in the archive
   * @return the size of the file, in bytes
   * @throws FileNotFoundException if <code>path</code> is not in the archive
   * @throws IOException oops
   */
  @Override
  public long getSize(String path) throws IOException {
    return Files.size(dir.resolve(path));
  }

  /**
   * Gets the modification time of a file in the archive, in milliseconds
   * since the epoch.
   *
   * @param path the path to the file in the archive
   * @return the mtime of the file
   * @throws FileNotFoundException if <code>path</code> is not in the archive
   * @throws IOException oops
   */
  @Override
  public long getMTime(String path) throws IOException {
    return Files.getLastModifiedTime(dir.resolve(path)).toMillis();
  }

  private List<String> getFilesImpl(Path root) throws IOException {
    try (Stream<Path> s = Files.walk(root, FileVisitOption.FOLLOW_LINKS)) {
      return s.filter(p -> !Files.isDirectory(p))
              .map(p -> dir.relativize(p).toString()) 
              .collect(Collectors.toList());
    }
  }

  /**
   * Gets the list of files in the archive.
   *
   * @return the list of files in the archive
   * @throws IOException oops
   */
  @Override
  public List<String> getFiles() throws IOException {
    return getFilesImpl(dir);
  }

  /**
   * Gets the list of files under a given directory of the archive.
   *
   * @param root the directory
   * @return the list of files under the given directory
   * @throws FileNotFoundException if <code>root</code> is not in the archive
   * @throws IOException oops
   */
  @Override
  public List<String> getFiles(String root) throws IOException {
    return getFilesImpl(dir.resolve(root));
  }
}
