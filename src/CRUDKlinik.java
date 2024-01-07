import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CRUDKlinik extends JFrame implements ActionListener {
    JTextField tfNama, tfAlamat, tfNIK, tfTanggalLahir;
    JButton btnTambah, btnUpdate, btnHapus, btnPrev, btnNext, btnDaftar, btnClose;
    JTable table;
    DefaultTableModel model;
    ArrayList<Pasien> listPasien;
    int currentRecord = 0;

    public CRUDKlinik() {
        listPasien = new ArrayList<>();

        setTitle("CRUD Data Pasien");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);

        JPanel panel = new JPanel();

        JLabel lblNama = new JLabel("Nama:");
        tfNama = new JTextField(20);

        JLabel lblAlamat = new JLabel("Alamat:");
        tfAlamat = new JTextField(50);

        JLabel lblNIK = new JLabel("NIK:");
        tfNIK = new JTextField(15);

        JLabel lblTanggalLahir = new JLabel("Tanggal Lahir (YYYY-MM-DD):");
        tfTanggalLahir = new JTextField(15);

        btnTambah = new JButton("Tambah");
        btnHapus = new JButton("Hapus");
        btnUpdate = new JButton("Update");
        btnPrev = new JButton("Sebelumnya");
        btnNext = new JButton("Selanjutnya");
        btnDaftar = new JButton("Daftar Pasien");
        btnClose = new JButton("Close");

        btnTambah.addActionListener(this);
        btnHapus.addActionListener(this);
        btnUpdate.addActionListener(this);
        btnPrev.addActionListener(this);
        btnNext.addActionListener(this);
        btnDaftar.addActionListener(this);
        btnClose.addActionListener(this);

        model = new DefaultTableModel();
        table = new JTable(model);
        model.addColumn("Nama");
        model.addColumn("Alamat");
        model.addColumn("NIK");
        model.addColumn("Tanggal Lahir");

        JScrollPane scrollPane = new JScrollPane(table);

        panel.add(lblNama);
        panel.add(tfNama);
        panel.add(lblAlamat);
        panel.add(tfAlamat);
        panel.add(lblNIK);
        panel.add(tfNIK);
        panel.add(lblTanggalLahir);
        panel.add(tfTanggalLahir);
        panel.add(btnTambah);
        panel.add(btnHapus);
        panel.add(btnUpdate);
        panel.add(btnPrev);
        panel.add(btnNext);
        panel.add(btnDaftar);
        panel.add(btnClose);
        panel.add(scrollPane);

        add(panel);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnTambah) {
            tambahData();
        } else if (e.getSource() == btnHapus) {
            hapusData();
        } else if (e.getSource() == btnUpdate) {
            updateData();
        } else if (e.getSource() == btnPrev) {
            navigatePrevious();
        } else if (e.getSource() == btnNext) {
            navigateNext();
        } else if (e.getSource() == btnDaftar) {
            tampilkanDaftarPasien();
        } else if (e.getSource() == btnClose) {
            System.exit(0);
        }
    }

    public void tambahData() {
        String nama = tfNama.getText();
        String alamat = tfAlamat.getText();
        String nik = tfNIK.getText();
        String tanggalLahirString = tfTanggalLahir.getText();

        if (cekPanjangField(nama, alamat, nik) && cekNIKunik(nik)) {
            try {
                SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdfOutput = new SimpleDateFormat("yyyy-MMM-dd");
                Date tanggalLahir = sdfInput.parse(tanggalLahirString);

                Object[] row = {nama, alamat, nik, sdfOutput.format(tanggalLahir)};
                model.addRow(row);
                listPasien.add(new Pasien(nama, alamat, nik, tanggalLahir));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        kosongkanField();
    }

    public boolean cekPanjangField(String nama, String alamat, String nik) {
        if (nama.length() <= 20 && alamat.length() <= 50 && nik.length() <= 15) {
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Inputan nama maksimal 20 karakter, alamat maksimal 50 karakter, NIK maksimal 15 karakter.");
            return false;
        }
    }


    public boolean cekNIKunik(String nik) {
        for (Pasien pasien : listPasien) {
            if (pasien.getNIK().equals(nik)) {
                JOptionPane.showMessageDialog(null, "NIK sudah ada!");
                return false;
            }
        }
        return true;
    }

    public void hapusData() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            listPasien.remove(selectedRow);
            model.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(null, "Pilih baris untuk dihapus.");
        }
    }

    public void updateData() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            String nama = tfNama.getText();
            String alamat = tfAlamat.getText();
            String nik = tfNIK.getText();
            String tanggalLahirString = tfTanggalLahir.getText();

            if (cekPanjangField(nama, alamat, nik) && (cekNIKunik(nik) || listPasien.get(selectedRow).getNIK().equals(nik))) {
                try {
                    SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat sdfOutput = new SimpleDateFormat("yyyy-MMM-dd");
                    Date tanggalLahir = sdfInput.parse(tanggalLahirString);

                    listPasien.get(selectedRow).setNama(nama);
                    listPasien.get(selectedRow).setAlamat(alamat);
                    listPasien.get(selectedRow).setNIK(nik);
                    listPasien.get(selectedRow).setTanggalLahir(tanggalLahir);

                    model.setValueAt(nama, selectedRow, 0);
                    model.setValueAt(alamat, selectedRow, 1);
                    model.setValueAt(nik, selectedRow, 2);
                    model.setValueAt(sdfOutput.format(tanggalLahir), selectedRow, 3);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Pilih baris untuk diperbarui.");
        }
    }

    public void navigatePrevious() {
        if (currentRecord > 0) {
            currentRecord--;
            tfNama.setText(listPasien.get(currentRecord).getNama());
            tfAlamat.setText(listPasien.get(currentRecord).getAlamat());
            tfNIK.setText(listPasien.get(currentRecord).getNIK());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            tfTanggalLahir.setText(sdf.format(listPasien.get(currentRecord).getTanggalLahir()));
        } else {
            JOptionPane.showMessageDialog(null, "Ini record pertama.");
        }
    }

    public void navigateNext() {
        if (currentRecord < listPasien.size() - 1) {
            currentRecord++;
            tfNama.setText(listPasien.get(currentRecord).getNama());
            tfAlamat.setText(listPasien.get(currentRecord).getAlamat());
            tfNIK.setText(listPasien.get(currentRecord).getNIK());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            tfTanggalLahir.setText(sdf.format(listPasien.get(currentRecord).getTanggalLahir()));
        } else {
            JOptionPane.showMessageDialog(null, "Ini record terakhir.");
        }
    }

    public void tampilkanDaftarPasien() {
        JFrame frame = new JFrame("Daftar Pasien");
        JPanel panel = new JPanel();

        JTextArea textArea = new JTextArea(20, 50);
        textArea.setEditable(false);

        for (Pasien pasien : listPasien) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd");
            String pasienString = "Nama: " + pasien.getNama() + "\nAlamat: " + pasien.getAlamat()
                    + "\nNIK: " + pasien.getNIK() + "\nTanggal Lahir: " + sdf.format(pasien.getTanggalLahir()) + "\n\n";
            textArea.append(pasienString);
        }

        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane);
        frame.add(panel);

        frame.setSize(600, 400);
        frame.setVisible(true);
    }

    public void kosongkanField() {
        tfNama.setText("");
        tfAlamat.setText("");
        tfNIK.setText("");
        tfTanggalLahir.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CRUDKlinik();
        });
    }
}

class Pasien {
    private String nama;
    private String alamat;
    private String NIK;
    private Date tanggalLahir;

    public Pasien(String nama, String alamat, String NIK, Date tanggalLahir) {
        this.nama = nama;
        this.alamat = alamat;
        this.NIK = NIK;
        this.tanggalLahir = tanggalLahir;
    }

    public String getNama() {
        return nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getNIK() {
        return NIK;
    }

    public Date getTanggalLahir() {
        return tanggalLahir;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public void setNIK(String NIK) {
        this.NIK = NIK;
    }

    public void setTanggalLahir(Date tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }
}









