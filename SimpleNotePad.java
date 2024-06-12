import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Arrays;

public class SimpleNotePad extends JFrame {
    private JTextArea textArea;
    private JFileChooser fileChooser;  // 文件选择器，用于打开和保存文件
    private String currentFile = "null";  // 当前文件名，初始为"nuill"
    private boolean textChanged = false;  // 用于判断文本是否发生了变化

    FileOutputStream fos;

    public SimpleNotePad() throws FileNotFoundException {
        super("记事本");
        fos = new FileOutputStream("design1\\Log.txt");

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setSize(600, 500);
        this.setLocationRelativeTo(null);

        textArea = new JTextArea();
        fileChooser = new JFileChooser();

        createMenuBar();
        createPopupMenu();

        textArea.setFont(new Font("SansSerif", Font.PLAIN, 16));
        textArea.getDocument().addDocumentListener(new DocumentListener() {  // 文档有更新则把textChanged变为true
            @Override
            public void changedUpdate(DocumentEvent e) {
                textChanged = true;
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                textChanged = true;
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                textChanged = true;
            }
        });

        this.add(new JScrollPane(textArea), BorderLayout.CENTER);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                clossing();
            }
        });

    }

    public SimpleNotePad(JTextArea textArea, JFileChooser fileChooser, String currentFile, boolean textChanged, FileOutputStream fos) {
        this.textArea = textArea;
        this.fileChooser = fileChooser;
        this.currentFile = currentFile;
        this.textChanged = textChanged;
        this.fos = fos;
    }


    /**
     * 本方法对“文件”菜单栏进行初始化，并添加动作监听事件
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("文件");
        JMenuItem newItem = new JMenuItem("新建");
        JMenuItem openItem = new JMenuItem("打开");
        JMenuItem saveItem = new JMenuItem("保存");

        JMenu Color = new JMenu("字体");
        JMenuItem changeColor = new JMenuItem("更换颜色");
        JMenuItem changeFontSize = new JMenuItem("更换大小");

        JMenu changeBackground = new JMenu("更改背景颜色");
        JMenuItem black = new JMenuItem("黑色");
        JMenuItem white = new JMenuItem("白色");
        JMenuItem red = new JMenuItem("红色");
        JMenuItem green = new JMenuItem("绿色");
        JMenuItem yellow = new JMenuItem("棕色");

        JMenu closed = new JMenu("关闭");
        JMenuItem close = new JMenuItem("退出");

        // 文件菜单
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);

        // 颜色菜单
        Color.add(changeColor);
        Color.add(changeFontSize);

        // 关闭菜单
        closed.add(close);

        // 背景菜单
        changeBackground.add(black);
        changeBackground.add(white);
        changeBackground.add(red);
        changeBackground.add(green);
        changeBackground.add(yellow);

        // 添加进菜单栏中
        menuBar.add(fileMenu);
        menuBar.add(Color);
        menuBar.add(changeBackground);
        menuBar.add(closed);

        this.setJMenuBar(menuBar);

        // 当点击新建时
        newItem.addActionListener(e -> {
            if (textChanged) {
                int option = JOptionPane.showConfirmDialog(
                        SimpleNotePad.this,
                        "您的文件已更改，是否保存？",
                        "保存文件？",
                        JOptionPane.YES_NO_CANCEL_OPTION
                );

                if (option == JOptionPane.YES_OPTION) {
                    try {
                        saveFile();
                    } catch (IOException ex) {
                        logException(ex);
                    }
                } else if (option == JOptionPane.NO_OPTION) {
                    textArea.setText("");
                    currentFile = "null";
                    textChanged = false;
                }
            } else {
                textArea.setText("");
                currentFile = "null";
                textChanged = false;
            }
        });

        // 当点击打开时
        openItem.addActionListener(e -> openFile());

        // 当点击保存时
        saveItem.addActionListener(e -> {
            try {
                saveFile();
            } catch (IOException ex) {
                logException(ex);
            }
        });

        // 当点击更改颜色时
        changeColor.addActionListener(e -> changeColor(textArea));

        // 当点击更改大小时
        changeFontSize.addActionListener(e -> changeFontSize(textArea));

        // 当点击退出时
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clossing();
            }
        });

        // 当点击更改背景颜色时
        black.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setBackground(java.awt.Color.black);
                try {
                    new changeBackgroundColor();
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        white.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setBackground(java.awt.Color.white);
            }
        });

        red.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setBackground(java.awt.Color.red);
            }
        });

        green.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setBackground(java.awt.Color.green);
            }
        });

        yellow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setBackground(java.awt.Color.orange);
            }
        });

    }

    /**
     * 本方法用于对空白区域进行右键操作
     */
    private void createPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem cutItem = new JMenuItem("剪切");
        JMenuItem copyItem = new JMenuItem("复制");
        JMenuItem pasteItem = new JMenuItem("粘贴");

        cutItem.addActionListener(e -> textArea.cut());
        copyItem.addActionListener(e -> textArea.copy());
        pasteItem.addActionListener(e -> textArea.paste());

        popupMenu.add(cutItem);
        popupMenu.add(copyItem);
        popupMenu.add(pasteItem);

        textArea.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    /**
     * 本方法用于打开一个文件选择对话框
     */
    private void openFile() {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(fileChooser.getSelectedFile()));
                textArea.read(reader, null);
                currentFile = fileChooser.getSelectedFile().getName();  // 获取选定的文件的名字
                textChanged = false;
            } catch (IOException e) {
                logException(e);
            }
        }
    }

    /**
     * 方法用于将文本区域（textArea）中的内容保存到文件中
     */
    private void saveFile() throws IOException {
        if (currentFile.equals("null")) {
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                currentFile = fileChooser.getSelectedFile().getName();  // getSelectedFile() --> 返回选中的文件
            } else {
                return;  // 结束方法
            }
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileChooser.getSelectedFile())); // getSelectedFile() --> 返回选中的文件
            textArea.write(writer);
            textChanged = false;
        } catch (IOException e) {
            logException(e);
        }
    }


    /**
     * 本方法用于记录异常
     *
     * @param e 异常
     */
    private void logException(Exception e) {
        String stackTrace = Arrays.toString(e.getStackTrace());
        try {
            fos.write(stackTrace.getBytes());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 通过颜色选择器选择字体颜色
     *
     * @param textArea 要改变颜色的文本区域
     */
    private void changeColor(JTextArea textArea) {
        // 弹出颜色选择器
        Color newColor = JColorChooser.showDialog(
                SimpleNotePad.this,  // 继承于JFrame的类的引用
                "请选择文本颜色",
                textArea.getForeground()
        );

        if (newColor != null) {
            textArea.setForeground(newColor);
        }
    }

    /**
     * 本方法用于改变字体大小
     *
     * @param textArea 要改变大小的文本框
     */
    private void changeFontSize(JTextArea textArea) {
        String[] fontSizes = {"10", "12", "14", "16", "18", "20", "22", "24", "26", "28", "30"};
        JComboBox<String> fontSizeComboBox = new JComboBox<>(fontSizes);
        fontSizeComboBox.addActionListener(e -> {
            String selectedSize = (String) fontSizeComboBox.getSelectedItem();
            Font currentFont = textArea.getFont();
            assert selectedSize != null;
            textArea.setFont(new Font(currentFont.getName(), currentFont.getStyle(), Integer.parseInt(selectedSize)));
        });

        // 添加到工具栏
        JToolBar toolBar = new JToolBar();
        toolBar.add(new JLabel("字体大小: "));
        toolBar.add(fontSizeComboBox);
        add(toolBar, BorderLayout.NORTH);

    }

    /**
     * 本方法用于退出程序时
     */
    private void clossing() {
        if (textChanged) {
            int option = JOptionPane.showConfirmDialog(SimpleNotePad.this,
                    "您的文件已更改，是否保存？",
                    "保存文件？",
                    JOptionPane.YES_NO_CANCEL_OPTION // 对话框将显示“是”、“否”和“取消”三个按钮
            );

            if (option == JOptionPane.YES_OPTION) {
                try {
                    saveFile();
                } catch (IOException e) {
                    logException(e);
                }
            } else if (option == JOptionPane.NO_OPTION) {
                System.exit(0);
            }
        } else {
            System.exit(0);
        }
    }




    /**
     * 获取
     * @return textArea
     */
    public JTextArea getTextArea() {
        return textArea;
    }

    /**
     * 设置
     * @param textArea
     */
    public void setTextArea(JTextArea textArea) {
        this.textArea = textArea;
    }

    /**
     * 获取
     * @return fileChooser
     */
    public JFileChooser getFileChooser() {
        return fileChooser;
    }

    /**
     * 设置
     * @param fileChooser
     */
    public void setFileChooser(JFileChooser fileChooser) {
        this.fileChooser = fileChooser;
    }

    /**
     * 获取
     * @return currentFile
     */
    public String getCurrentFile() {
        return currentFile;
    }

    /**
     * 设置
     * @param currentFile
     */
    public void setCurrentFile(String currentFile) {
        this.currentFile = currentFile;
    }

    /**
     * 获取
     * @return textChanged
     */
    public boolean isTextChanged() {
        return textChanged;
    }

    /**
     * 设置
     * @param textChanged
     */
    public void setTextChanged(boolean textChanged) {
        this.textChanged = textChanged;
    }

    /**
     * 获取
     * @return fos
     */
    public FileOutputStream getFos() {
        return fos;
    }

    /**
     * 设置
     * @param fos
     */
    public void setFos(FileOutputStream fos) {
        this.fos = fos;
    }

    public String toString() {
        return "SimpleNotePad{textArea = " + textArea + ", fileChooser = " + fileChooser + ", currentFile = " + currentFile + ", textChanged = " + textChanged + ", fos = " + fos + "}";
    }
}
