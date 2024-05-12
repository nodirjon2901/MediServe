package org.example.mediserve.bot;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.mediserve.domain.dto.request.UserCreateDTO;
import org.example.mediserve.domain.entity.UserEntity;
import org.example.mediserve.domain.enums.UserRole;
import org.example.mediserve.domain.enums.UserState;
import org.example.mediserve.service.FileService;
import org.example.mediserve.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MediBot extends TelegramLongPollingBot {

    private final UserService userService;

    private final BotService botService;

    private final FileService imageService;

    @Value("${bot.token}")
    private String botToken;

    @Value("${bot.username}")
    private String botUsername;

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            Message message = update.getMessage();
            String text = message.getText();
            Long chatId = message.getChatId();

            Optional<UserEntity> currentUser = userService.findByChatId(chatId);
            UserState userState = UserState.START;

            if (currentUser.isPresent()) {
                userState = currentUser.get().getState();

                switch (userState) {

                    case FIRSTNAME -> {
                        userService.updateFirstname(chatId, text);
                        userService.updateState(chatId, UserState.LASTNAME);
                        userState = UserState.LASTNAME;
                    }

                    case LASTNAME -> {
                        userService.updateLastname(chatId, text);
                        userService.updateState(chatId, UserState.USERNAME);
                        userState = UserState.USERNAME;
                    }

                    case USERNAME -> {
                        int result = userService.updateUsername(chatId, text);
                        if (result == 200) {
                            userService.updateState(chatId, UserState.PASSWORD);
                            userState = UserState.PASSWORD;
                        } else if (result == 500) {
                            userService.updateState(chatId, UserState.EXIST_USERNAME);
                            userState=UserState.EXIST_USERNAME;
                        }
                    }

                    case EXIST_USERNAME -> {
                        int result = userService.updateUsername(chatId, text);
                        if (result == 200) {
                            userService.updateState(chatId, UserState.PASSWORD);
                            userState = UserState.PASSWORD;
                        }
                    }

                    case PASSWORD -> {
                        userService.updatePassword(chatId, text);
                        userService.updateState(chatId, UserState.EXPERIENCE);
                        userState = UserState.EXPERIENCE;
                    }
                    case EXPERIENCE -> {
                        userService.updateExperience(chatId, text);
                        userService.updateState(chatId, UserState.SENDPICTURE);
                        userState = UserState.SENDPICTURE;
                    }

                    case SENDPICTURE -> {
                        if (message.hasPhoto()) {
                            List<PhotoSize> photoSizes = message.getPhoto();
                            for (PhotoSize photo : photoSizes) {
                                String fileId = photo.getFileId();
                                GetFile getFile = new GetFile(fileId);
                                try {
                                    File file = execute(getFile);
                                    SendPhoto sendPhoto = new SendPhoto();
                                    sendPhoto.setChatId(chatId);
                                    sendPhoto.setPhoto(new InputFile(file.getFileId()));
                                    UserEntity userEntity = userService.findByChatId(chatId).get();
                                    sendPhoto.setCaption("Ma'lumotlaringizni tekshiring..." + "\n" +
                                            "Ism: " + userEntity.getFirstName() + "\n" +
                                            "Familya: " + userEntity.getLastName() + "\n" +
                                            "Tajribasi: " + userEntity.getExperience() + "\n" +
                                            "Telefon nomeri: " + userEntity.getPhoneNumber());
                                    sendPhoto.setReplyMarkup(botService.editMenu());
                                    execute(sendPhoto);
                                } catch (TelegramApiException e) {
                                    e.printStackTrace();
                                }
                                userService.updateState(chatId, UserState.FINISHED);
                                userState = UserState.FINISHED;
                                return;
                            }
                        }
                    }

//                    case TRANSACTION_INFORMATION -> {
//                        if (message.hasPhoto()) {
//                            userService.updateState(chatId, UserState.TRANSACTION_FINISHED);
//                            userState = UserState.TRANSACTION_FINISHED;
//                        }
//
//                    }

                }


            } else if (message.hasContact()) {
                Contact contact = message.getContact();
                UserCreateDTO userCreateDTO = UserCreateDTO.builder()
                        .chatId(chatId)
                        .firstName(contact.getFirstName())
                        .lastName(contact.getLastName())
                        .phoneNumber(contact.getPhoneNumber())
                        .role(UserRole.GUEST)
                        .isPaid(false)
                        .state(UserState.REGISTERED)
                        .build();
                userService.save(userCreateDTO);
                userState = UserState.REGISTERED;
            }

            switch (userState) {

                case START -> {
                    execute(botService.register(chatId.toString()));
                }

                case REGISTERED -> {
                    execute(botService.removeReplyKeyboard(chatId.toString()));
                    execute(botService.menu(chatId.toString()));
                }

                case LASTNAME -> {
                    execute(botService.enterLastname(chatId.toString()));
                }

                case USERNAME -> {
                    execute(botService.enterUsername(chatId.toString()));
                }

                case EXIST_USERNAME -> {
                    execute(botService.enterOtherUsername(chatId.toString()));
                }

                case PASSWORD -> {
                    execute(botService.enterPassword(chatId.toString()));
                }

                case EXPERIENCE -> {
                    execute(botService.enterExperience(chatId.toString()));
                }

                case SENDPICTURE -> {
                    execute(botService.sendPicture(chatId.toString()));
                }

                case TRANSACTION_INFORMATION -> {
                    execute(botService.sendTransactionInfo(chatId.toString()));
                }

//                case TRANSACTION_FINISHED -> {
//                    execute(botService.sendSuccessMessage(chatId.toString()));
//                }

            }

        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();
            Message message = (Message) callbackQuery.getMessage();
            Long chatId = message.getChatId();

            Optional<UserEntity> currentUser = userService.findByChatId(chatId);
            UserState userState = UserState.START;

            if (currentUser.isPresent()) {
                userState = currentUser.get().getState();

                switch (userState) {

                    case REGISTERED -> {
                        switch (data) {

                            case "yes" -> {
                                userService.updateState(chatId, UserState.FIRSTNAME);
                                userState = UserState.FIRSTNAME;
                            }

                            case "no" -> {
                                userService.updateState(chatId, UserState.NOTREADY);
                                userState = UserState.NOTREADY;
                            }

                        }
                    }

                    case FINISHED -> {
                        switch (data) {

                            case "save" -> {
                                userService.updateState(chatId, UserState.PAYMENT);
                                userState = UserState.PAYMENT;
                            }

                            case "edit" -> {
                                userService.updateState(chatId, UserState.FIRSTNAME);
                                userState = UserState.FIRSTNAME;
                            }

                        }
                    }

                    case PAYMENT -> {

                        switch (data) {

                            case "yes" -> {
                                userService.updateState(chatId, UserState.TRANSACTION_INFORMATION);
                                userState = UserState.TRANSACTION_INFORMATION;
                            }

                            case "no" -> {
                                userService.updateState(chatId, UserState.NOTREADY);
                                userState = UserState.NOTREADY;
                            }

                        }

                    }
                }
            }

            switch (userState) {

                case START -> {
                    execute(botService.register(chatId.toString()));
                }

                case REGISTERED -> {
                    execute(botService.menu(chatId.toString()));
                }

                case FIRSTNAME -> {
                    execute(botService.enterFirstName(chatId.toString()));
                }

                case PAYMENT -> {
                    execute(botService.paymentOffer(chatId.toString()));
                }

                case TRANSACTION_INFORMATION -> {
                    execute(botService.sendTransactionInfo(chatId.toString()));
                }

                case NOTREADY -> {
                    execute(botService.goodBye(chatId.toString()));
                    userService.deleteByChatId(chatId);
                }

            }

        }

    }
}
