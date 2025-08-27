package kr.hhplus.be.server.order.application.event;

import kr.hhplus.be.server.order.application.service.DataPlatFormService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class DataPlatformEventListener {
    private DataPlatFormService dataPlatFormService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleDataPlatformEvent(DataFlatformEvent event) {
        dataPlatFormService.sendOrderDataToDataPlatForm(event.getOrder());
    }
}
