package abc.sancu;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;

public class DeleteFileUtil {

    public static void main(String[] args) {
        String path = "F:\\test";//要删除的文件所在目录
        String fileSuffixes = "";//指定要删除的文件后缀格式(#格式1#格式2#格式3#...格式n#),可为null，表示删除所有
        boolean delSubFile = true;//是否删除子文件
        boolean keepEmptyFolder = true;//是否保留空文件夹
        DeleteFileUtil util = DeleteFileUtil.getDeleteFileUtil(path, fileSuffixes, delSubFile, keepEmptyFolder);
        int count = util.handler();
        System.out.println("删除" + count + "个文件");
    }

    private SimpleFileVisitor<Path> sFileVisitor;
    private String datasOrginalPath;

    private DeleteFileUtil(int type, String oldPath, String fileSuffixes, boolean delSubFile,
            boolean keepEmptyFolder) {
        this.datasOrginalPath = oldPath;
        sFileVisitor = new DeleteFileVisitor(oldPath, fileSuffixes, delSubFile, keepEmptyFolder);
    }

    public int handler() {
        Path start = Paths.get(datasOrginalPath);
        try {
            Files.walkFileTree(start, sFileVisitor);
        } catch (IOException e) {
            e.printStackTrace();
        }
        DeleteFileVisitor dfv = (DeleteFileVisitor) sFileVisitor;
        return dfv.getResult();
    }

    /**
     *
     * @param originalPath 要删除的文件夹路径
     * @param fileSuffixes
     * 指定要删除的文件后缀格式(#格式1#格式2#格式3#...格式n#),可为null，表示删除所有,可为""空字符串，表示删除所有无后缀文件
     * @param delSubFile 是否删除子文件
     * @param keepEmptyFolder 是否保留空文件夹
     * @return 返回删除个数
     */
    public static DeleteFileUtil getDeleteFileUtil(String originalPath, String fileSuffixes,
            boolean delSubFile, boolean keepEmptyFolder) {
        DeleteFileUtil ihonestFileUtil = new DeleteFileUtil(2, originalPath, fileSuffixes,
                delSubFile, keepEmptyFolder);
        return ihonestFileUtil;
    }
    
     public static DeleteFileUtil 删除文件(String originalPath, String fileSuffixes,
            boolean delSubFile, boolean keepEmptyFolder) {
        DeleteFileUtil ihonestFileUtil = new DeleteFileUtil(2, originalPath, fileSuffixes,
                delSubFile, keepEmptyFolder);
        return ihonestFileUtil;
    }

}
