package tadjik.ilyosjon;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

public class Mybot extends TelegramLongPollingBot {
    private MyDatabase myDatabase=new MyDatabase();
    private MyBotService myBotService = new MyBotService();
    private Map<Long, String> vazifaAddState = new HashMap<>();
    private Map<String, String> vazifaAddInfo = new HashMap<>();
    private Map<Long, String> vazifaDeleteState = new HashMap<>();
    private Map<String, String> vazifaDeleteInfo = new HashMap<>();
    private Map<Long, String> vazifaUpdateState = new HashMap<>();
    private Map<String, String> vazifaUpdateInfo = new HashMap<>();
    @Override
    public void onUpdateReceived(Update update) {
        String text = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();

        if (update.hasMessage() && update.getMessage().hasText()) {
            if (text.equals("/start")) {
                try {
                    execute(myBotService.start(chatId));
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
            if (text.equals("creatvazifa")) {
                switch (vazifaAddState.getOrDefault(chatId, "start")) {
                    case "start":
                        sendMessage(chatId, "Id ni kiriting:");
                        vazifaAddState.put(chatId, "name");
                        break;
                }
            } else if ("name".equals(vazifaAddState.get(chatId))) {
                vazifaAddInfo.put(chatId + "id", text);
                sendMessage(chatId, "Nomini kiriting:");
                vazifaAddState.put(chatId, "sana");
            } else if ("sana".equals(vazifaAddState.get(chatId))) {
                vazifaAddInfo.put(chatId + "name", text);
                sendMessage(chatId, "Sanani kiriting:");
                vazifaAddState.put(chatId, "status");
            } else if ("status".equals(vazifaAddState.get(chatId))) {
                vazifaAddInfo.put(chatId + "sana", text);
                sendMessage(chatId, "Statusni kiriting:");
                vazifaAddState.put(chatId, "finish");
            } else if ("finish".equals(vazifaAddState.get(chatId))) {
                vazifaAddInfo.put(chatId + "status", text);
                int id = Integer.parseInt(vazifaAddInfo.get(chatId + "id"));
                String name = vazifaAddInfo.get(chatId + "name");
                String sana = vazifaAddInfo.get(chatId + "sana");
                String status = vazifaAddInfo.get(chatId + "status");

                Vazifalar vazifa = new Vazifalar(id, name, sana, status);
                myDatabase.creatvazifa(vazifa);
                sendMessage(chatId, "Vazifa saqlandi!");
                vazifaAddState.remove(chatId);
            }
            if (text.equals("Vazifalarni korish")) {
                String s = myDatabase.readvazifa();
                sendMessage(chatId, s);
            }
            if (text.equals("Vazifani ochirish")) {
                switch (vazifaDeleteState.getOrDefault(chatId, "start")) {
                    case "start":
                        sendMessage(chatId, "O'chirmoqchi bolgan vazifaning idsini kiriting:");
                        vazifaDeleteState.put(chatId, "finish");
                        break;
                }
            } else if ("finish".equals(vazifaDeleteState.get(chatId))) {
                vazifaDeleteInfo.put(chatId + "id", text);
                int id = Integer.parseInt(vazifaDeleteInfo.get(chatId + "id"));
                myDatabase.deletevazifa(id);
                sendMessage(chatId, "Vazifa ochirildi!");
                vazifaDeleteState.remove(chatId);
            }
            if (text.equals("Vazifa update qilish")){
                switch (vazifaUpdateState.getOrDefault(chatId, "start")) {
                    case "start":
                        sendMessage(chatId, "Qaysi ID dagi vazifani ozgartirmoqchisiz!");
                        vazifaUpdateState.put(chatId, "name");
                        break;
                }
            }
            else if ("name".equals(vazifaUpdateState.get(chatId))) {
                vazifaUpdateInfo.put(chatId + "id", text);
                sendMessage(chatId, "Yangi name kiriting:");
                vazifaUpdateState.put(chatId, "finish");

            } else if ("finish".equals(vazifaUpdateState.get(chatId))) {
                vazifaUpdateInfo.put(chatId + "name", text);
                int id = Integer.parseInt(vazifaUpdateInfo.get(chatId + "id"));
                String name = vazifaUpdateInfo.get(chatId + "name");

                myDatabase.updatevazifa(id, name);
                sendMessage(chatId, "Vazifa nomi ozgartirildi!");
                vazifaUpdateState.remove(chatId);
            }
        }
    }

        public void sendMessage (Long chatId, String text){
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId.toString());
            sendMessage.setText(text);
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    @Override
    public String getBotUsername() {
        return "@IlyosjonProject_bot";
    }

    @Override
    public String getBotToken() {
        return "7657833917:AAHpOBHTwJMPZDlDEz14A1vAIMdWluyq7wM";
    }
}
