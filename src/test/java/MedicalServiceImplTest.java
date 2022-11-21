import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.medical.MedicalService;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MedicalServiceImplTest {
    @Test
    void checkBloodPressureTest() {
        PatientInfo patientInfo = new PatientInfo("e6f821b6-1518-47f7-ba82-b8e50c02dee6", "Иван",
                "Петров", LocalDate.of(1980, 11, 26),
                new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80)));
        String expectedMessage = String.format("Warning, patient with id: %s, need help", patientInfo.getId());

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById(patientInfo.getId()))
                .thenReturn(patientInfo);

        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);
        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);

        medicalService.checkBloodPressure(patientInfo.getId(), new BloodPressure(140, 80));

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        Mockito.verify(sendAlertService).send(argumentCaptor.capture());
        String actualMessage = argumentCaptor.getValue();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void checkTemperatureTest() {
        PatientInfo patientInfo = new PatientInfo("ee3f8f39-a043-4ac2-ae9b-0efe91db77f7", "Семен",
                "Михайлов", LocalDate.of(1982, 1, 16),
                new HealthInfo(new BigDecimal("36.6"), new BloodPressure(125, 78)));
        String expectedMessage = String.format("Warning, patient with id: %s, need help", patientInfo.getId());

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById(patientInfo.getId()))
                .thenReturn(patientInfo);

        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);
        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);

        medicalService.checkTemperature(patientInfo.getId(), new BigDecimal("39"));

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        Mockito.verify(sendAlertService).send(argumentCaptor.capture());
        String actualMessage = argumentCaptor.getValue();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void notMessageTest() {
        PatientInfo patientInfo = new PatientInfo("e6f821b6-1518-47f7-ba82-b8e50c02dee6", "Иван",
                "Петров", LocalDate.of(1980, 11, 26),
                new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80)));
        String expectedMessage = String.format("Warning, patient with id: %s, need help", patientInfo.getId());

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById(patientInfo.getId()))
                .thenReturn(patientInfo);

        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);
        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkTemperature(patientInfo.getId(), new BigDecimal("36.0"));
        medicalService.checkBloodPressure(patientInfo.getId(), new BloodPressure(120, 80));

        Mockito.verify(sendAlertService, Mockito.times(0)).send(expectedMessage);

    }
}
