package uz.optimit.taxi.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.optimit.taxi.entity.*;
import uz.optimit.taxi.entity.Enum.Gender;
import uz.optimit.taxi.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static uz.optimit.taxi.entity.Enum.Constants.*;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RegionRepository regionRepository;
    private final CityRepository cityRepository;
    private final AutoCategoryRepository autoCategoryRepository;
    private final AutoModelRepository autoModelRepository;
    private final StatusRepository statusRepository;

    @Value("${spring.sql.init.mode}")
    private String initMode;

    @Override
    public void run(String... args) {

        if (initMode.equals("always")) {
            Role admim = new Role(1, ADMIN);
            Role save15 = roleRepository.save(admim);
            Role yolovchi = new Role(2, PASSENGER);
            Role save16 = roleRepository.save(yolovchi);
            Role haydovchi = new Role(3, DRIVER);
            Role save17 = roleRepository.save(haydovchi);

            User admin = User.builder()
                    .fullName("ADMIN")
                    .phone("111111111")
                    .birthDate(LocalDate.parse("1998-05-13"))
                    .gender(Gender.ERKAK)
                    .registeredDate(LocalDateTime.now())
                    .verificationCode(0)
                    .verificationCodeLiveTime(null)
                    .password(passwordEncoder.encode("111111"))
                    .isBlocked(true)
                    .build();
            User save = userRepository.save(admin);
            statusRepository.save(Status.builder().user(save).stars(5L).count(1L).build());
            save.setRoles(List.of(save15, save16, save17));
            userRepository.save(save);

            List<Region> regions = List.of(
                    new Region(1, "Toshkent shahri")
                    , new Region(2, "Toshkent viloyati")
                    , new Region(3, "Andijon")
                    , new Region(4, "Buxoro")
                    , new Region(5, "Farg`ona")
                    , new Region(6, "Qoraqalpog‘iston")
                    , new Region(7, "Jizzax")
                    , new Region(8, "Qashqadaryo")
                    , new Region(9, "Navoiy")
                    , new Region(10, "Namangan")
                    , new Region(11, "Samarqand")
                    , new Region(12, "Surxondaryo")
                    , new Region(13, "Sirdaryo")
                    , new Region(14, "Xorazm"));

            regionRepository.saveAll(regions);

            Region save1 = regionRepository.getById(1);
            Region save2 = regionRepository.getById(2);
            Region save3 = regionRepository.getById(3);
            Region save4 = regionRepository.getById(4);
            Region save5 = regionRepository.getById(5);
            Region save6 = regionRepository.getById(6);
            Region save7 = regionRepository.getById(7);
            Region save8 = regionRepository.getById(8);
            Region save9 = regionRepository.getById(9);
            Region save10 = regionRepository.getById(10);
            Region save11 = regionRepository.getById(11);
            Region save12 = regionRepository.getById(12);
            Region save13 = regionRepository.getById(13);
            Region save14 = regionRepository.getById(14);


            List<City> cities = List.of(
                    new City("Bektemir t", save1),
                    new City("Mirzo Ulug‘bek t", save1),
                    new City("Mirobod t", save1),
                    new City("Olmazor t", save1),
                    new City("Sirg‘ali t", save1),
                    new City("Uchtepa t", save1),
                    new City("Chilonzor t", save1),
                    new City("Shayxontohur t", save1),
                    new City("Yunusobod t", save1),
                    new City("Yakkasaroy t", save1),
                    new City("Yashnobod t", save1),


                    new City("Nurafshon sh", save2),
                    new City("Angren sh", save2),
                    new City("Bekobod sh", save2),
                    new City("Olmaliq sh", save2),
                    new City("Ohangaron sh", save2),
                    new City("Chirchiq sh", save2),
                    new City("Yangiyo‘l sh", save2),
                    new City("Bekobod t", save2),
                    new City("Bo‘ka t", save2),
                    new City("Bo‘stonliq t", save2),
                    new City("Zangiota t", save2),
                    new City("Qibray t", save2),
                    new City("Quyichirchiq t", save2),
                    new City("Oqqo‘rg‘on t", save2),
                    new City("Ohangaron t", save2),
                    new City("Parkent t", save2),
                    new City("Piskent t", save2),
                    new City("Toshkent t", save2),
                    new City("O‘rtachirchiq t", save2),
                    new City("Chinoz t", save2),
                    new City("Yuqorichirchiq t", save2),
                    new City("Yangiyo‘l t", save2),


                    new City("Andijon sh", save3),
                    new City("Xonabod sh", save3),
                    new City("Andijon t", save3),
                    new City("Asaka t", save3),
                    new City("Baliqchi t", save3),
                    new City("Bo‘z t", save3),
                    new City("Buloqboshi t", save3),
                    new City("Jalaquduq t", save3),
                    new City("Izboskan t", save3),
                    new City("Qo‘rg‘ontepa t", save3),
                    new City("Marhamat t.", save3),
                    new City("Oltinko‘l t", save3),
                    new City("Paxtaobod t", save3),
                    new City("Ulug‘nor t", save3),
                    new City("Xo‘jaobod t", save3),
                    new City("Shxon t", save3),


                    new City("Buxoro sh", save4),
                    new City("Kogon sh", save4),
                    new City("Buxoro t", save4),
                    new City("Vobkent t", save4),
                    new City("Jondor t", save4),
                    new City("Kogon t", save4),
                    new City("Olot t", save4),
                    new City("Peshku t", save4),
                    new City("Romitan t", save4),
                    new City("Shofirkon t", save4),
                    new City("Qorovulbozor t", save4),
                    new City("Qorako‘l t", save4),
                    new City("G‘ijduvon t", save4),


                    new City("Farg‘ona sh", save5),
                    new City("Marg‘ilon sh", save5),
                    new City("Quvasoy sh", save5),
                    new City("Qo‘qon sh", save5),
                    new City("Beshariq t", save5),
                    new City("Bog‘dod t", save5),
                    new City("Buvayda t", save5),
                    new City("Dang‘ara t", save5),
                    new City("Yozyovon t", save5),
                    new City("Quva t", save5),
                    new City("Qo‘shtepa t", save5),
                    new City("Oltiariq t", save5),
                    new City("Rishton t", save5),
                    new City("So‘x t", save5),
                    new City("Toshloq t", save5),
                    new City("O‘zbekiston t", save5),
                    new City("Uchko‘prik t", save5),
                    new City("Farg‘ona t", save5),
                    new City("Furqat t", save5),


                    new City("Nukus sh", save6),
                    new City("Amudaryo t", save6),
                    new City("Beruniy t", save6),
                    new City("Kegeyli t", save6),
                    new City("Qanliko‘l t", save6),
                    new City("Qorao‘zak t", save6),
                    new City("Qo‘ng‘irot t", save6),
                    new City("Mo‘ynoq t", save6),
                    new City("Nukus t", save6),
                    new City("Taxiatosh t", save6),
                    new City("Taxtako‘pir t", save6),
                    new City("To‘rtko‘l t", save6),
                    new City("Xo‘jayli t", save6),
                    new City("Chimboy t", save6),
                    new City("Sho‘manoy t", save6),
                    new City("Ellikqal’a t", save6),


                    new City("Jizzax sh", save7),
                    new City("Arnasoy t", save7),
                    new City("Baxmal t", save7),
                    new City("Do‘stlik t", save7),
                    new City("Zarbdor t", save7),
                    new City("Zafarobod t", save7),
                    new City("Zomin t", save7),
                    new City("Mirzacho‘l t", save7),
                    new City("Paxtakor t", save7),
                    new City("Forish t", save7),
                    new City("Sharof Rashidov t", save7),
                    new City("G‘allaorol t", save7),
                    new City("Yangiobod t", save7),


                    new City("Qarshi sh", save8),
                    new City("Shsabz sh", save8),
                    new City("Dehqonobod t", save8),
                    new City("Kasbi t", save8),
                    new City("Kitob t", save8),
                    new City("Koson t", save8),
                    new City("Mirishkor t", save8),
                    new City("Muborak t", save8),
                    new City("Nishon t", save8),
                    new City("Chiroqchi t", save8),
                    new City("Shsabz t", save8),
                    new City("Yakkabog‘ t", save8),
                    new City("Qamashi t", save8),
                    new City("Qarshi t", save8),
                    new City("G‘uzor t", save8),


                    new City("Navoiy sh", save9),
                    new City("Zarafshon sh", save9),
                    new City("Karmana t", save9),
                    new City("Konimex t", save9),
                    new City("Navbahor t", save9),
                    new City("Nurota t", save9),
                    new City("Tomdi t", save9),
                    new City("Uchquduq t", save9),
                    new City("Xatirchi t", save9),
                    new City("Qiziltepa t", save9),


                    new City("Namangan sh", save10),
                    new City("Kosonsoy t", save10),
                    new City("Mingbuloq t", save10),
                    new City("Namangan t", save10),
                    new City("Norin t", save10),
                    new City("Pop t", save10),
                    new City("To‘raqo‘rg‘on t", save10),
                    new City("Uychi t", save10),
                    new City("Uchqo‘rg‘on t", save10),
                    new City("Chortoq t", save10),
                    new City("Chust t", save10),
                    new City("Yangiqo‘rg‘on t", save10),


                    new City("Samarqand sh", save11),
                    new City("Kattaqo‘rg‘on sh", save11),
                    new City("Bulung‘ur t", save11),
                    new City("Jomboy t", save11),
                    new City("Ishtixon t", save11),
                    new City("Kattaqo‘rg‘on t", save11),
                    new City("Narpay t", save11),
                    new City("Nurobod t", save11),
                    new City("Oqdaryo t", save11),
                    new City("Payariq t", save11),
                    new City("Pastdarg‘om t", save11),
                    new City("Paxtachi t", save11),
                    new City("Samarqand t", save11),
                    new City("Toyloq t", save11),
                    new City("Urgut t", save11),
                    new City("Qo‘shrabot t", save11),


                    new City("Termiz sh", save12),
                    new City("Angor t", save12),
                    new City("Boysun t", save12),
                    new City("Denov t", save12),
                    new City("Jarqo‘rg‘on t", save12),
                    new City("Muzrobod t", save12),
                    new City("Oltinsoy t", save12),
                    new City("Sariosiyo t", save12),
                    new City("Termiz t", save12),
                    new City("Uzun t", save12),
                    new City("Sherobod t", save12),
                    new City("Sho‘rchi t", save12),
                    new City("Qiziriq t", save12),
                    new City("Qumqo‘rg‘on t", save12),


                    new City("Guliston sh", save13),
                    new City("Yangiyer sh", save13),
                    new City("Shirin sh", save13),
                    new City("Boyovut t", save13),
                    new City("Guliston t", save13),
                    new City("Mirzaobod t", save13),
                    new City("Oqoltin t", save13),
                    new City("Sardoba t", save13),
                    new City("Sayxunobod t", save13),
                    new City("Sirdaryo t", save13),
                    new City("Xovos t", save13),


                    new City("Urganch sh", save14),
                    new City("Xiva sh", save14),
                    new City("Bog‘ot t", save14),
                    new City("Gurlan t", save14),
                    new City("Urganch t", save14),
                    new City("Xiva t", save14),
                    new City("Xonqa t", save14),
                    new City("Hazorasp t", save14),
                    new City("Shovot t", save14),
                    new City("Yangiariq t", save14),
                    new City("Yangibozor t", save14),
                    new City("Qo‘shko‘pir t", save14));

            cityRepository.saveAll(cities);


            List<AutoCategory> categories = List.of(
                    new AutoCategory(1, "GM", true)
                    , new AutoCategory(2, "BMW", true)
                    , new AutoCategory(3, "Toyota", true)
                    , new AutoCategory(4, "Volkswagen", true)
                    , new AutoCategory(5, "Chevrolet", true)
                    , new AutoCategory(6, "Ford", true)
                    , new AutoCategory(7, "Mazda", true)
                    , new AutoCategory(8, "Audi", true)
                    , new AutoCategory(9, "Kia", true)
                    , new AutoCategory(10, "Hyudai", true)
                    , new AutoCategory(11, "Honda", true)
                    , new AutoCategory(12, "Mercedes-Benz", true)
                    , new AutoCategory(13, "Volvo", true)
                    , new AutoCategory(14, "Daewoo", true)
                    , new AutoCategory(15, "Lexus", true)
                    , new AutoCategory(16, "Nissan", true)
                    , new AutoCategory(17, "Opel", true)
                    , new AutoCategory(18, "Tesla", true)
                    , new AutoCategory(19, "Lada", true));
            autoCategoryRepository.saveAll(categories);
            AutoCategory autoCategory1 = autoCategoryRepository.getById(1);
            AutoCategory autoCategory12 = autoCategoryRepository.getById(12);

            AutoModel autoModel1 = new AutoModel("Damas", (byte) 4, autoCategory1, true);
            AutoModel autoModel2 = new AutoModel("Cobalt", (byte) 4, autoCategory1, true);
            AutoModel autoModel3 = new AutoModel("Lacetti-Gentra", (byte) 4, autoCategory1, true);
            AutoModel autoModel4 = new AutoModel("Lacetti", (byte) 4, autoCategory1, true);
            AutoModel autoModel5 = new AutoModel("Nexia ", (byte) 4, autoCategory1, true);
            AutoModel autoModel6 = new AutoModel("Nexia 2", (byte) 4, autoCategory1, true);
            AutoModel autoModel7 = new AutoModel("Nexia 3", (byte) 4, autoCategory1, true);
            AutoModel autoModel8 = new AutoModel("Spark", (byte) 4, autoCategory1, true);
            AutoModel autoModel9 = new AutoModel("Matiz", (byte) 4, autoCategory1, true);
            AutoModel autoModel10 = new AutoModel("Captiva", (byte) 6, autoCategory1, true);
            AutoModel autoModel11 = new AutoModel("Epica", (byte) 4, autoCategory1, true);
            AutoModel autoModel12 = new AutoModel("Malibu ", (byte) 4, autoCategory1, true);
            AutoModel autoModel13 = new AutoModel("Malibu 2", (byte) 4, autoCategory1, true);
            AutoModel autoModel14 = new AutoModel("Orlando", (byte) 6, autoCategory1, true);
            AutoModel autoModel15 = new AutoModel("Tracker", (byte) 4, autoCategory1, true);
            AutoModel autoModel16 = new AutoModel("TrailBlazer", (byte) 6, autoCategory1, true);
            AutoModel autoModel17 = new AutoModel("Equinox", (byte) 4, autoCategory1, true);

            autoModelRepository.saveAll(List.of(autoModel1,
                    autoModel2, autoModel3, autoModel4,
                    autoModel5, autoModel6, autoModel7,
                    autoModel8, autoModel9, autoModel10,
                    autoModel11, autoModel12, autoModel13,
                    autoModel14, autoModel15, autoModel16,
                    autoModel17));
            AutoModel autoModel18 = new AutoModel("Gelik", (byte) 4, autoCategory12, true);
            AutoModel autoModel19 = new AutoModel("G8", (byte) 4, autoCategory12, true);
            autoModelRepository.saveAll(List.of(autoModel18, autoModel19));
        }
    }
}