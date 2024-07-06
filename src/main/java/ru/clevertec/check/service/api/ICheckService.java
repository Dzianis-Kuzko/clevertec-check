package main.java.ru.clevertec.check.service.api;

import main.java.ru.clevertec.check.core.Check;
import main.java.ru.clevertec.check.core.dto.CreateCheckDTO;
import main.java.ru.clevertec.check.printer.IPrinter;

public interface ICheckService {
    Check createCheck(CreateCheckDTO createCheckDTO);

    void print(String message, IPrinter printer);
}
