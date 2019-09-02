package ru.cbr.turing.dump.infodiod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class StorageService {

    private Application.Config config;

    public StorageService(@Autowired Application.Config config) {
        this.config = config;
    }

    public List<String> filesCreatedByOrder(String orderNum) {
        List<String> result = new ArrayList<String>();

        OrderFile dmpFile = new OrderFile(new OrderFilePath(orderNum, config.getDmpFileExtension()));
        OrderFile logFile = new OrderFile(new OrderFilePath(orderNum, config.getLogFileExtension()));

        dmpFile.printFileNameIfFileExist(result);
        logFile.printFileNameIfFileExist(result);

        return result;
    }

    private class OrderFilePath {

        private final String orderNum;
        private final String fileExtension;

        public OrderFilePath(String orderNum, String fileExtension) {
            this.orderNum = orderNum;
            this.fileExtension = fileExtension;
        }

        public String path() {
            StringBuilder sb = new StringBuilder();
            sb.append(config.getStorageMountPath()).append("/").append(orderNum).append(fileExtension);
            return sb.toString();
        }
    }

    private class OrderFile {

        private final OrderFilePath filePath;
        private final File file;

        public OrderFile(OrderFilePath filePath) {
            this.filePath = filePath;
            this.file = new File(filePath.path());
        }

        public void printFileNameIfFileExist(List<String> printTo) {
            if (file.exists()) {
                printTo.add(file.getName());
            }
        }
    }
}
