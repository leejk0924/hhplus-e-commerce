package kr.hhplus.be.server.config.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public abstract class DispatchableEvent extends ApplicationEvent {
    @Getter
    private final boolean async;

    public DispatchableEvent(Object source) {
        this(source, true);
    }
    public DispatchableEvent(Object source, final boolean async) {
        super(source);
        this.async = async;
    }
}
