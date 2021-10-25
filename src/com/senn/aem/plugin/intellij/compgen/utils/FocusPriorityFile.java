package com.senn.aem.plugin.intellij.compgen.utils;

import java.io.File;
import java.util.Comparator;

/**
 * empty FIle subclass
 */
public class FocusPriorityFile extends File {

    public static final Comparator<File> COMPARATOR = new PriorityComparator();

    private int focusPriority = -1;

    public FocusPriorityFile(String path, int focusPriority) {
        super(path);
        this.focusPriority = focusPriority;
    }

    public int getFocusPriority() {
        return focusPriority;
    }

    static class PriorityComparator implements Comparator<File> {

        @Override
        public int compare(File file1, File file2) {
            if(file1 == null && file2 == null) return 0;
            if(file1 == null) return 1;
            if(file2 == null) return -1;

            if(file1 instanceof FocusPriorityFile && file2 instanceof FocusPriorityFile) {
                FocusPriorityFile focus1 = (FocusPriorityFile) file1;
                FocusPriorityFile focus2 = (FocusPriorityFile) file2;

                if(focus1.getFocusPriority() == focus2.getFocusPriority()) return 0;

                if(focus1.getFocusPriority() == -1) return 1;

                if(focus2.getFocusPriority() == -1) return -1;

                if(focus2.getFocusPriority() < focus1.getFocusPriority()) return -1;

                if(focus1.getFocusPriority() < focus2.getFocusPriority()) return 1;
            } else if(file1 instanceof FocusPriorityFile) {
                return 1;
            } else if(file2 instanceof FocusPriorityFile) {
                return -1;
            } else {
                return 0;
            }
            return 0;
        }
    }
}
