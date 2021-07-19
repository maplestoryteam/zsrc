package abc.sancu;

import java.io.File;

public class FileDemo_05 {
	// 判断删除指定的文件或文件夹是否成功，成功则返回true否则返回false
	public static boolean deleteAnyone(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			System.out.println("文件删除失败：" + fileName + "文件不存在！");
			return false;
		} else {
			if (file.isFile()) {
				return FileDemo_05.deleteFiles(fileName);
			} else {
				return FileDemo_05.deleteDir(fileName);
			}
		}
	}
        public static boolean 删除文件(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			System.out.println("文件删除失败：" + fileName + "文件不存在！");
			return false;
		} else {
			if (file.isFile()) {
				return FileDemo_05.deleteFiles(fileName);
			} else {
				return FileDemo_05.deleteDir(fileName);
			}
		}
	}
	// 判断删除指定的文件是否成功，成功则返回true否则返回false
	public static boolean deleteFiles(String fileName) {
		File file = new File(fileName);
		// 如果文件路径对应的文件存在，并且是一个文件，则直接删除。
		if (file.exists() && file.isFile()) {
            //System.out.println("文件：" + fileName + "删除成功！");
            //System.out.println("文件" + fileName + "删除失败！");
            return file.delete();
		} else {
			//System.out.println("文件删除失败：" + fileName + "文件不存在！");
			return false;
		}
	}
	// 判断删除指定的目录（文件夹）以及目录下的文件是否成功，成功则返回true否则返回false
	public static boolean deleteDir(String dir) {
		// 如果dir不以文件分隔符结尾，自动添加文件分隔符。
		if (!dir.endsWith(File.separator)) {
			dir = dir + File.separator;
		}
		File dirFile = new File(dir);
		// 如果dir表示的文件不存在，或者不是一个文件夹，则退出
		if (!dirFile.exists() || (!dirFile.isDirectory())) {
			System.out.println("目录删除失败：" + dir + "目录不存在！");
			return false;
		}
		boolean flag = true;
		// 删除指定目录下所有文件（包括子目录）
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除文件
			if (files[i].isFile()) {
				flag = FileDemo_05.deleteFiles(files[i].getAbsolutePath());
				if (!flag) {
					break;
				}
			}
			// 删除子目录
			else if (files[i].isDirectory()) {
				flag = FileDemo_05.deleteDir(files[i].getAbsolutePath());
				if (!flag) {
					break;
				}
			}
		}
		if (!flag) {
			System.out.println("删除目录失败！");
			return false;
		}
		// 删除当前目录
		if (dirFile.delete()) {
			System.out.println("目录：" + dir + "删除成功！");
			return true;
		} else {
			return false;
		}
	}
	public static void main(String[] args) {
		// 删除文件
		System.out.println("调用deleteFiles方法的结果如下：");
		String file = "D:\\temp\\aa\\bb\\ccFile.txt";
		FileDemo_05.deleteFiles(file);
		System.out.println();
		// 删除目录
		System.out.println("调用deleteDir方法的结果如下：");
		String dir = "D:\\temp\\key";
		FileDemo_05.deleteDir(dir);
		System.out.println();
		// 删除文件或目录
		System.out.println("调用deleteAnyone方法的结果如下：");
		dir = "D:\\temp\\read";
		FileDemo_05.deleteAnyone(dir);
	}
}
