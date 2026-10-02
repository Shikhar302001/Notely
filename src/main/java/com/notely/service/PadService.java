package com.notely.service;

import com.notely.Repository.PadRepository;
import com.notely.entity.Pad;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PadService {

    private final PadRepository padRepository;

    public PadService(PadRepository padRepository) {
        this.padRepository = padRepository;
    }

    public Pad getOrCreatePad(String id) {

        return padRepository.findById(id)
                .orElseGet(() -> {

                    Pad pad = new Pad(
                            id,
                            "",
                            LocalDateTime.now()
                    );

                    return padRepository.save(pad);
                });
    }

    public Pad updatePad(
            String id,
            String content,
            Long version) {

        Pad pad = getOrCreatePad(id);

        if (!version.equals(pad.getVersion())) {
            throw new VersionConflictException();
        }

        pad.setContent(content);
        pad.setUpdatedAt(LocalDateTime.now());

        return padRepository.save(pad);
    }

    public void deletePad(String id) {
        padRepository.deleteById(id);
    }

    public static class VersionConflictException
            extends RuntimeException {

        public VersionConflictException() {
            super("Pad version conflict");
        }
    }
}
