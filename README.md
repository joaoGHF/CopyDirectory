<h1>CopyDirectory</h1>

<p>The <a href="src/copy_directory/CopyDirectory.java">CopyDirectory</a> is the main class of this program. This program question the user in your init with two <code>JOptionPane#showInputDialog(java.awt.Component, Object, String, int)</code> that resquest the values of the <code>String copyString</code> and <code>String pasteString</code> and his respectives paths (<code>copyPath</code> and <code>pastePath</code>). Then it will create a <code>StringBuilder</code> named <code>sbLog</code> that receive all logs of this copy, like the init time, all files copied and pasted and, the end time.</p>

<p>With the <code>Files#walk(Path, java.nio.file.FileVisitOption...)</code> that returs a <code>Stream</code> of <code>Path</code>, this program will try traverse this <code>Stream&#60;Path&#62;</code> with a <code>Stream#forEach(Consumer)</code>. As this <code>Consumer</code> is passed a <code>lambda</code> with the parameter <code>p</code> of type <code>Path</code> and int the body the process of copy and paste.</p>

<p>Into the body of the consumer, the program will try create a <code>Path</code> named <code>q</code> that receive the <code>pastePath.resolve(copyPath.relativize(p))</code>. Then print what file is being copied and what file is being pasted. Next, test with the <code>Files#isDirectory(Path, java.nio.file.LinkOption...)</code> if <code>p</code> (the path being copied) is a directory, if the test is true, is created the directory <code>q</code> (the path being pasted) with the method <code>Files#createDirectory(Path, java.nio.file.attribute.FileAttribute...)</code>; else the file on the path <code>p</code> is copied to the path <code>q</code> with the method <code>Files#copy(Path, Path, java.nio.file.CopyOption...</code>.</p>
  
<p>After the scope of the <code>try</code> that contains the <code>Files#walk(Path, java.nio.file.FileVisitOption...)</code> is terminated and processed the log, finally it is writed.</p>
  
<p>Finally is showed in a <code>JOptionPane#showConfirmDialog(java.awt.Component, Object, String, int)</code> a message notifying the finish of the copy from <code>copyPath</code> to <code>pasteString</code>.</p>
