package edu.medical.demo.config;

import edu.medical.demo.model.Department;
import edu.medical.demo.model.Hospital;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class HospitalDataInitializer {

    private final SessionFactory sessionFactory;

    public void initializeData() {
        try (Session session = sessionFactory.openSession()) {
            final Transaction transaction = session.beginTransaction();

            final Hospital hospital1 = new Hospital("Центральная городская больница", "ул. Центральная, 123");
            final Hospital hospital2 = new Hospital("Северный медицинский центр", "ул. Липовая, 456");
            final Hospital hospital3 = new Hospital("Больница Зеленая долина", "ул. Дубовая, 789");

            final Department neuro1 = new Department("Неврологическое отделение", "ул. Невро, 123", "д-р Смирнов", hospital1);
            final Department therapy1 = new Department("Терапевтическое отделение", "ул. Терапия, 124", "д-р Иванова", hospital1);
            final Department cardio1 = new Department("Кардиологическое отделение", "ул. Кардио, 125", "д-р Петров", hospital1);

            final Department neuro2 = new Department("Неврологический центр", "ул. Нейро, 456", "д-р Сидоров", hospital2);
            final Department pediatric2 = new Department("Педиатрическое отделение", "ул. Детская, 457", "д-р Кузнецова", hospital2);
            final Department surgical2 = new Department("Хирургическое отделение", "ул. Хирургия, 458", "д-р Васильев", hospital2);

            final Department neuro3 = new Department("Центр нейронаук", "ул. Мозговая, 789", "д-р Крылов", hospital3);
            final Department maternity3 = new Department("Родильное отделение", "ул. Материнства, 790", "д-р Соколова", hospital3);
            final Department oncology3 = new Department("Онкологическое отделение", "ул. Онкология, 791", "д-р Белова", hospital3);

            hospital1.setDepartments(Arrays.asList(neuro1, therapy1, cardio1));
            hospital2.setDepartments(Arrays.asList(neuro2, pediatric2, surgical2));
            hospital3.setDepartments(Arrays.asList(neuro3, maternity3, oncology3));

            session.persist(hospital1);
            session.persist(hospital2);
            session.persist(hospital3);

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
