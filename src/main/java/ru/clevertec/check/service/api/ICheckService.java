package ru.clevertec.check.service.api;

import ru.clevertec.check.core.Check;
import ru.clevertec.check.core.dto.CreateCheckDTO;
import ru.clevertec.check.printer.IPrinter;

public interface ICheckService {
    Check createCheck(CreateCheckDTO createCheckDTO);

}
