package copy_directory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.function.Consumer;
import java.util.stream.Stream;

import javax.swing.JOptionPane;

/**
 * <h1>
 * CopyDirectory
 * </h1>
 * 
 * 
 * <p>
 * The {@code CopyDirectory} is the main class of this program. This program
 * question the user in your init with two
 * {@link JOptionPane#showInputDialog(java.awt.Component, Object, String, int)}
 * that resquest the values of the {@code copyString} and {@code pasteString}
 * and his respectives paths ({@code copyPath} and {@code pastePath}). Then it
 * will create a {@link StringBuilder} named {@code sbLog} that receive all logs
 * of this copy, like the init time, all files copied and pasted and, the end
 * time.
 * </p>
 * 
 * <p>
 * To prevent an infinite recursion error, caused when the {@code pastePath} is
 * inside the {@code copyPath}, this program then checks for the existence of
 * this possible error in a {@code try block}. First is stored in two
 * {@link String} objects the absolute path of {@code copyPath} and
 * {@code pastePath}. Then verify if {@code absPastePath} contains
 * {@code absCopyPath} with the method {@link String#contains(CharSequence)}.
 * If it is true, is throwed a new
 * {@link InputMismatchException#InputMismatchException(String)}, that receive
 * in the {@link String} parameter a detail message that explains the error.
 * Then, in the {@code catch} block, is showed a
 * {@link JOptionPane#showMessageDialog(java.awt.Component, Object, String, int)}
 * that warns of the error and terminate the program with a
 * {@link System#exit(int)} as parameter {@code -1} to indicates unsuccessful
 * termination with Exception.
 * </p>
 * 
 * <p>
 * With the {@link Files#walk(Path, java.nio.file.FileVisitOption...)} that
 * returs a {@link Stream} of {@link Path}, this program will try traverse this
 * {@code Stream<Path>} with a {@link Stream#forEach(Consumer)}. As this
 * {@link Consumer} is passed a {@code lambda} with the parameter {@code p} of
 * type {@link Path} and int the body the process of copy and paste.
 * </p>
 * 
 * <p>
 * Into the body of the consumer, the program will try create a {@link Path}
 * named {@code q} that receive the
 * {@code pastePath.resolve(copyPath.relativize(p))}. Then print what file is
 * being copied and what file is being pasted. Next, test with the
 * {@link Files#isDirectory(Path, java.nio.file.LinkOption...)} if {@code p}
 * (the path being copied) is a directory, if the test is true, is created the
 * directory {@code q} (the path being pasted) with the method
 * {@link Files#createDirectory(Path, java.nio.file.attribute.FileAttribute...)};
 * else the file on the path {@code p} is copied to the path {@code q} with the
 * method {@link Files#copy(Path, Path, java.nio.file.CopyOption...)}.
 * </p>
 * 
 * <p>
 * After the scope of the {@code try} that contains the
 * {@link Files#walk(Path, java.nio.file.FileVisitOption...)} is terminated and
 * processed the log, finally it is writed.
 * </p>
 * 
 * <p>
 * Finally is showed in a
 * {@link JOptionPane#showConfirmDialog(java.awt.Component, Object, String, int)}
 * a message notifying the finish of the copy from {@code copyPath} to
 * {@code pasteString}.
 * </p>
 * 
 * 
 * 
 * @implSpec
 *           <h2>
 *           Warning:
 *           </h2>
 * 
 *           <p>
 *           A bug occurs when is called from copy one directory to another
 *           inside the copied directory, causing recursivity. In the model:
 *           copy {@code path/src/} to {@code path/src/test}
 *           </p>
 * 
 * @author joaoGHF
 * @version 1.0, 13/12/2023
 */
public class CopyDirectory {
    public static void main(String[] args) {
        String copyString = JOptionPane.showInputDialog(null, "Enter the diretory to copy:", "CopyDirectory",
                JOptionPane.QUESTION_MESSAGE);
        Path copyPath = Path.of(copyString);

        String pasteString = JOptionPane.showInputDialog(null, "Enter the diretory to receive the copy (the target):",
                "CopyDirectory", JOptionPane.QUESTION_MESSAGE);
        Path pastePath = Path.of(pasteString);

        try {
            String absCopyPath = copyPath.toAbsolutePath().toString();
            String absPastePath = pastePath.toAbsolutePath().toString();

            if (absPastePath.contains(absCopyPath)) {
                throw new InputMismatchException(
                        String.format(
                                "Endless recusivity caused because%n" +
                                        "the paste directory '%s'%n" +
                                        "is inside the copy directory '%s'",
                                absPastePath, absCopyPath));
            }

        } catch (InputMismatchException e1) {
            JOptionPane.showMessageDialog(null, e1.getMessage(), "Warning! Error...", JOptionPane.WARNING_MESSAGE);
        }

        StringBuilder sbLog = new StringBuilder();
        sbLog.append(String.format("InitTime=%s%n", LocalDateTime.now()));

        try {
            Files.walk(copyPath).forEach(p -> {
                try {
                    Path q = pastePath.resolve(copyPath.relativize(p));
                    System.out.println(p + "->" + q);
                    if (Files.isDirectory(p)) {
                        Files.createDirectory(q);
                        sbLog.append(String.format("CreateDirectory=>%s%n", q));
                    } else {
                        Files.copy(p, q);
                        sbLog.append(String.format("CopiedFile=>%s->%s%n", p, q));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        sbLog.append(String.format("EndTime=%s%n----%n", LocalDateTime.now()));

        Path logPath = Path.of("src", "logs", "export.log");
        System.out.println(logPath);
        System.out.println(logPath.toAbsolutePath());
        byte[] logContent = sbLog.toString().getBytes(StandardCharsets.UTF_8);

        try {
            if (!logPath.toFile().exists()) {
                Files.createFile(logPath);
            }
            Files.write(logPath, logContent, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JOptionPane.showConfirmDialog(null,
                String.format("Terminated copy from %n '%s' %n to %n'%s'.", copyPath, pasteString), "CopyDirectory",
                JOptionPane.YES_OPTION);
    }
}
