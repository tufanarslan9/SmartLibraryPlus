package app;

import dao.BookDao;
import dao.StudentDao;
import dao.LoanDao;
import entity.Book;
import entity.Student;
import entity.Loan;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    static BookDao bookDao = new BookDao();
    static StudentDao studentDao = new StudentDao();
    static LoanDao loanDao = new LoanDao();

    static Scanner scanner = new Scanner(System.in);

    // Kullanıcıdan güvenli integer okuma
    static int readInt(String message) {
        while (true) {
            try {
                System.out.print(message);
                String input = scanner.nextLine();
                return Integer.parseInt(input);
            } catch (Exception e) {
                System.out.println("Lütfen geçerli bir sayı giriniz.");
            }
        }
    }

    public static void main(String[] args) {

        while (true) {
            System.out.println("\n===== SmartLibraryPlus =====");
            System.out.println("1 - Kitap Ekle");
            System.out.println("2 - Kitapları Listele");
            System.out.println("3 - Öğrenci Ekle");
            System.out.println("4 - Öğrencileri Listele");
            System.out.println("5 - Kitap Ödünç Ver");
            System.out.println("6 - Ödünç Listesini Görüntüle");
            System.out.println("7 - Kitap Geri Teslim Al");
            System.out.println("0 - Çıkış");

            int secim = readInt("Seçiminiz: ");

            switch (secim) {
                case 1 -> kitapEkle();
                case 2 -> kitapListele();
                case 3 -> ogrenciEkle();
                case 4 -> ogrenciListele();
                case 5 -> kitapOduncVer();
                case 6 -> oduncListe();
                case 7 -> kitapIade();
                case 0 -> {
                    System.out.println("Çıkış yapılıyor...");
                    System.exit(0);
                }
                default -> System.out.println("Hatalı seçim!");
            }
        }
    }

    // 1 - Kitap Ekle
    static void kitapEkle() {
        System.out.print("Kitap adı: ");
        String title = scanner.nextLine();

        System.out.print("Yazar: ");
        String author = scanner.nextLine();

        int year = readInt("Yıl: ");

        Book book = new Book(title, author, year);
        bookDao.save(book);

        System.out.println("Kitap eklendi.");
    }

    // 2 - Kitapları Listele
    static void kitapListele() {
        List<Book> books = bookDao.getAll();

        for (Book b : books) {
            System.out.println(b.getId() + " - " + b.getTitle() + " | " + b.getAuthor() + " | " + b.getStatus());
        }
    }

    // 3 - Öğrenci Ekle
    static void ogrenciEkle() {
        System.out.print("Öğrenci adı: ");
        String name = scanner.nextLine();

        System.out.print("Bölüm: ");
        String dept = scanner.nextLine();

        Student s = new Student(name, dept);
        studentDao.save(s);

        System.out.println("Öğrenci eklendi.");
    }

    // 4 - Öğrencileri Listele
    static void ogrenciListele() {
        List<Student> students = studentDao.getAll();

        for (Student s : students) {
            System.out.println(s.getId() + " - " + s.getName() + " (" + s.getDepartment() + ")");
        }
    }

    // 5 - Kitap Ödünç Ver
    static void kitapOduncVer() {
        int sid = readInt("Öğrenci ID: ");
        int bid = readInt("Kitap ID: ");

        Student s = studentDao.getById(sid);
        Book b = bookDao.getById(bid);

        if (s == null) {
            System.out.println("Bu ID'ye ait öğrenci bulunamadı!");
            return;
        }

        if (b == null) {
            System.out.println("Bu ID'ye ait kitap bulunamadı!");
            return;
        }

        if (b.getStatus().equals("BORROWED")) {
            System.out.println("Bu kitap zaten ödünç verilmiş!");
            return;
        }

        Loan loan = new Loan();
        loan.setStudent(s);
        loan.setBook(b);
        loan.setBorrowDate(LocalDate.now());

        b.setStatus("BORROWED");

        loanDao.save(loan);
        bookDao.update(b);

        System.out.println("Kitap ödünç verildi.");
    }

    // 6 - Ödünç Listesi
    static void oduncListe() {
        List<Loan> loans = loanDao.getAll();

        if (loans.isEmpty()) {
            System.out.println("Henüz ödünç kaydı bulunmamaktadır.");
            return;
        }

        for (Loan l : loans) {

            String studentName = (l.getStudent() != null) ? l.getStudent().getName() : "Bilinmiyor";
            String bookTitle = (l.getBook() != null) ? l.getBook().getTitle() : "Bilinmiyor";

            System.out.println(
                    l.getId() + " | " +
                            studentName + " | " +
                            bookTitle + " | " +
                            l.getBorrowDate() + " | " +
                            l.getReturnDate()
            );
        }
    }



    // 7 - Kitap Geri Teslim
    static void kitapIade() {
        int lid = readInt("Loan ID: ");

        Loan loan = loanDao.getById(lid);

        if (loan == null) {
            System.out.println("Bu ID'ye ait ödünç kaydı bulunamadı!");
            return;
        }

        loan.setReturnDate(LocalDate.now());

        Book b = loan.getBook();
        b.setStatus("AVAILABLE");

        loanDao.update(loan);
        bookDao.update(b);

        System.out.println("Kitap iade edildi.");
    }
}
