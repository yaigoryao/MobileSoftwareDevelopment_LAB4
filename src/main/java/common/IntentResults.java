package common;

public class IntentResults
{
    public enum Codes
    {
        OK("ОК"),
        CANCELED("Отменено"),
        ERROR("Ошибка");

        private String _description;
        Codes(String description)
        {
            _description = description;
        }
    }
}
