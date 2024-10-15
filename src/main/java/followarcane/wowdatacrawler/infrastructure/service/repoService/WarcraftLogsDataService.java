package followarcane.wowdatacrawler.infrastructure.service.repoService;

import followarcane.wowdatacrawler.domain.model.WarcraftLogsData;
import followarcane.wowdatacrawler.infrastructure.repository.WarcraftLogsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WarcraftLogsDataService {

    private final WarcraftLogsRepository warcraftLogsRepository;

    public WarcraftLogsDataService(WarcraftLogsRepository warcraftLogsRepository) {
        this.warcraftLogsRepository = warcraftLogsRepository;
    }

    public List<WarcraftLogsData> getAllLogs() {
        return warcraftLogsRepository.findAll();
    }

    public Optional<WarcraftLogsData> getLogById(Long id) {
        return warcraftLogsRepository.findById(id);
    }

    public WarcraftLogsData saveLog(WarcraftLogsData warcraftLogsData) {
        return warcraftLogsRepository.save(warcraftLogsData);
    }

    public void deleteLog(Long id) {
        warcraftLogsRepository.deleteById(id);
    }

    public void deleteAll() {
        warcraftLogsRepository.deleteAll();
    }
}
