package abc.sancu;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class DeleteFileVisitor extends SimpleFileVisitor<Path> {

    private String sourcePath;
    private String fileSuffixes;
    private boolean delSubFile;
    private boolean keepEmptyFolder;
    private String fileSuffix, tmpString;
    private File fileTmp;
    private boolean tmpBuer;
    private int delFileTotalCount;
    private int tryCount;
    private Path rootPath;

    public DeleteFileVisitor(String oldPath, String fileSuffixes, boolean delSubFile, boolean keepEmptyFolder) {
        sourcePath = oldPath;
        if (fileSuffixes != null) {
            this.fileSuffixes = fileSuffixes.trim().toLowerCase();
        }
        this.delSubFile = delSubFile;
        this.keepEmptyFolder = keepEmptyFolder;
        this.rootPath = Paths.get(sourcePath);
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        if (delSubFile || isRootPath(dir)) {
            return FileVisitResult.CONTINUE;
        } else {
            return FileVisitResult.SKIP_SUBTREE;
        }
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

        if (fileSuffixes != null) {
            tmpString = file.toFile().getName();
            if (!isDelBySuffix(tmpString)) {
                return FileVisitResult.CONTINUE;
            }

            if (!fileSuffixes.contains(fileSuffix)) {
                // 不符合指定文件格式的跳过
                return FileVisitResult.CONTINUE;
            }
        }
        fileTmp = file.toFile();
        tmpBuer = delFile(fileTmp);
        if (tmpBuer) {
            delFileTotalCount++;
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        if (isRootPath(dir)) {
            //根目录
            if (!keepEmptyFolder) {
                dir.toFile().delete();
            }
        } else //子目录
        {
            if (!keepEmptyFolder) {
                dir.toFile().delete();
            }
        }

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        exc.printStackTrace();
        return super.visitFileFailed(file, exc);
    }

    private boolean delFile(File file) {
        tryCount = 0;
        boolean tmpBuer = false;
        while (!(tmpBuer = file.delete()) && tryCount < 4) {
            //尝试四次删除，四次过后依旧删除不了跳过
            try {
                Thread.sleep(500 * 1);
                tryCount++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return tmpBuer;
    }

    public int getResult() {
        return delFileTotalCount;
    }

    /**
     * 是否根目录
     *
     * @param path
     * @return
     */
    private boolean isRootPath(Path path) {
        if (path == null) {
            return false;
        }
        return this.rootPath.equals(path);
    }

    /**
     * 根据指定后缀格式字符串判断一个文件是否要删除
     *
     * @param fileName 待判定的文件名称
     * @return 如果要删除返回true，否则返回false
     */
    private boolean isDelBySuffix(String fileName) {
        if (fileName == null) {
            return false;
        }

        if (fileSuffixes == null) {
            //删除所有
            return true;
        } else {
            //删除指定文件
            if (fileName.lastIndexOf(".") >= 0) {
//					获取当前文件后缀
                fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();
            } else {
                fileSuffix = "";
            }
            if ("".equals(fileSuffixes)) {
                //删除无后缀文件
                if ("".equals(fileSuffix)) {
                    return true;
                }
            } else //删除指定后缀文件
            {
                if (fileSuffixes.contains("#" + fileSuffix + "#")) {
                    //在后缀前后加#号是为了避免一些误判，比如：使用者要删除recmp3后缀格式，那么输入参数字符串为#recmp3#,这时遍历到mp3文件，在前后不加#的情况下，用contains就会误判
                    return true;
                }
            }
        }

        return false;
    }
}
