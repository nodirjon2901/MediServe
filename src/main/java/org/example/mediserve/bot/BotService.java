package org.example.mediserve.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BotService {

    public SendMessage register(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId,
                "Assalomu alaykum. Ro'yhatdan o'tish " +
                        "uchun telefon raqamingizni kiriting");
        sendMessage.setReplyMarkup(shareContact());
        return sendMessage;
    }

    public ReplyKeyboardMarkup shareContact() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        KeyboardRow row = new KeyboardRow();
        KeyboardButton button = new KeyboardButton();
        button.setText("Telefon raqamingizni kiriting");
        button.setRequestContact(true);
        row.add(button);
        replyKeyboardMarkup.setKeyboard(List.of(row));
        return replyKeyboardMarkup;
    }

    public SendMessage menu(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId,
                "Tasdiqlash orqali ma'lumotlaringizni to'ldiring");
        sendMessage.setReplyMarkup(inlineMenu());
        return sendMessage;
    }

    public InlineKeyboardMarkup inlineMenu() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton button = new InlineKeyboardButton("Tasdiqlash");
        button.setCallbackData("yes");
        row.add(button);

        button = new InlineKeyboardButton("Botdan chiqish");
        button.setCallbackData("no");
        row.add(button);
        rows.add(row);
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public ReplyKeyboardRemove replyKeyboardRemove() {
        return new ReplyKeyboardRemove(true);
    }

    public SendMessage enterFirstName(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "Ismingizni kiriting");
        sendMessage.setReplyMarkup(replyKeyboardRemove());
        return sendMessage;
    }

    public InlineKeyboardMarkup editMenu() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton button = new InlineKeyboardButton("Saqlash");
        button.setCallbackData("save");
        row.add(button);

        button = new InlineKeyboardButton("Tahrirlash");
        button.setCallbackData("edit");
        row.add(button);
        rows.add(row);
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public SendMessage enterLastname(String chatId) {
        return new SendMessage(chatId, "Familyangizni kiriting");
    }

    public SendMessage enterUsername(String chatId) {
        return new SendMessage(chatId, "O'zingiz uchun username kiriting");
    }

    public SendMessage enterOtherUsername(String chatId) {
        return new SendMessage(chatId, "Siz kiritgan username foydalanilgan. Iltimos boshqa username kiriting");
    }

    public SendMessage enterPassword(String chatId) {
        return new SendMessage(chatId, "O'rnatiladigan parolni kiriting");
    }

    public SendMessage enterExperience(String chatId) {
        return new SendMessage(chatId, "Tajribangizni kiriting");
    }

    public SendMessage sendPicture(String chatId) {
        return new SendMessage(chatId, "Rasmingizni yuboring");
    }


    public SendMessage removeReplyKeyboard(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId,
                "Shaxsiy va professional ma'lumotlaringizni kiritishga tayyormisiz?");
        sendMessage.setReplyMarkup(replyKeyboardRemove());
        return sendMessage;
    }

    public SendMessage goodBye(String chatId) {
        return new SendMessage(chatId,
                "Botimizdan foydalanganingiz uchun rahmat. Sog' bo'ling");

    }

    public SendMessage paymentOffer(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId,
                "Ma'lumotlaringiz asosiy bazaga saqlanishi uchun to'lov qilishga rozimisiz");
        sendMessage.setReplyMarkup(paymentOfferMarkup());
        return sendMessage;
    }

    public InlineKeyboardMarkup paymentOfferMarkup() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton button = new InlineKeyboardButton("Roziman");
        button.setCallbackData("yes");
        row.add(button);

        button = new InlineKeyboardButton("Botdan chiqish");
        button.setCallbackData("no");
        row.add(button);
        rows.add(row);
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public SendMessage sendTransactionInfo(String chatId) {
        return new SendMessage(chatId,
                "O'tkaziladigan summa: 1$" + "\n" +
                        "Ushbu link orqali to'lovni amalga oshirishingiz mumkin: http://localhost:8080/pay/payment-intent" + "\n\n"
                        );
    }


}
