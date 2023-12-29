### Запуск

Для локального запуска достаточно зайти в `Main` и заменить значения количества кораблей. В `observer.setPriers()` подаётся cуммарное количество кораблей одного и того же типа, которые будут добавляться в исполнение с помощью `observer.LaunchShips(ShipType, ShipCapacity, count)` (причалы должны знать, сколько кораблей им надо обработать, чтобы понимать, в какой момент остановить исполнение).

### Логирование

Пример из `Main` логируется как в консоль, так и в файл `logs/CommonLogger.log`. Каждый из тестов логируется в свой файл вида `logs/CommonLogger_<TestName>.log`.

### Логика работы

Потоками в программе являются корабли, причалы и `Main`. Корабль внутри себя делает 2 вещи: проходит через туннель и заходит в порт. Порт хранит в себе очередь необработанных кораблей и обрабатывает их по одному, пока количество обработанных кораблей не станет равно тому количеству, которое было передано в `observer.setPriers()` в начале программы.
