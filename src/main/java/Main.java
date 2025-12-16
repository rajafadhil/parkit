import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ParkingManager manager = new ParkingManager();
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        System.out.println("🚀 ParkIt - Sistem Manajemen Parkir Terminal");
        System.out.println("============================================");

        while (running) {
            System.out.println("\n[1] Parkir Masuk");
            System.out.println("[2] Parkir Keluar");
            System.out.println("[3] Daftarkan Langganan");
            System.out.println("[4] Laporan Hari Ini");
            System.out.println("[5] Keluar");
            System.out.print("Pilih: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Plat: "); String plat = sc.nextLine();
                    System.out.print("Jenis (MOTOR/MOBIL/TRUK): "); String jenis = sc.nextLine();
                    System.out.print("Spot (REGULER/PREMIUM/LANGGANAN): "); String spot = sc.nextLine();
                    manager.registerVehicle(plat, jenis, "User");
                    boolean success = manager.park(new Vehicle(plat, jenis, "User"), spot);
                    System.out.println(success ? "✅ Parkir berhasil!" : "❌ Gagal parkir.");
                    break;

                case 2:
                    System.out.print("Plat: "); String outPlat = sc.nextLine();
                    ParkingSession session = manager.unpark(outPlat);
                    if (session != null) {
                        System.out.println("✅ Keluar. Tarif: Rp" + (int)session.getFee());
                    } else {
                        System.out.println("❌ Kendaraan tidak ditemukan.");
                    }
                    break;

                case 3:
                    System.out.print("Plat: "); String subPlat = sc.nextLine();
                    manager.registerSubscription(subPlat, LocalDate.now(), LocalDate.now().plusMonths(1));
                    System.out.println("✅ Langganan 1 bulan aktif!");
                    break;

                case 4:
                    System.out.println("📊 Kendaraan Aktif: " + manager.getActiveVehicleCount());
                    System.out.println("💰 Pendapatan Hari Ini: Rp" + (int)manager.getTodayRevenue());
                    break;

                case 5:
                    running = false;
                    System.out.println("👋 Terima kasih!");
                    break;
            }
        }
        sc.close();
    }
}