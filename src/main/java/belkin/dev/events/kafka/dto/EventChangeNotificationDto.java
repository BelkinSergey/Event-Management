package belkin.dev.events.kafka.dto;

public class EventChangeNotificationDto<T> {

    private String name;

    private T oldField;

    private T newField;


    public EventChangeNotificationDto(String name, T oldField, T newField) {
        this.name = name;
        this.oldField = oldField;
        this.newField = newField;
    }


    public String getName() {
        return name;
    }

    public T getOldField() {
        return oldField;
    }

    public T getNewField() {
        return newField;
    }
}
