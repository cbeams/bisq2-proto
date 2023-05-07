/*
 * This file is part of Bisq.
 *
 * Bisq is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * Bisq is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Bisq. If not, see <http://www.gnu.org/licenses/>.
 */

package bisq.chat;

import bisq.chat.bisqeasy.channel.BisqEasyChatChannelSelectionService;
import bisq.chat.bisqeasy.channel.priv.BisqEasyPrivateTradeChatChannelService;
import bisq.chat.bisqeasy.channel.pub.BisqEasyPublicChatChannelService;
import bisq.chat.channel.ChatChannel;
import bisq.chat.channel.ChatChannelDomain;
import bisq.chat.channel.ChatChannelSelectionService;
import bisq.chat.channel.ChatChannelService;
import bisq.chat.channel.priv.PrivateChatChannel;
import bisq.chat.channel.priv.TwoPartyPrivateChatChannelService;
import bisq.chat.channel.pub.CommonPublicChatChannel;
import bisq.chat.channel.pub.CommonPublicChatChannelService;
import bisq.chat.message.ChatMessage;
import bisq.common.application.Service;
import bisq.common.util.CompletableFutureUtils;
import bisq.network.NetworkService;
import bisq.persistence.PersistableStore;
import bisq.persistence.PersistenceService;
import bisq.security.pow.ProofOfWorkService;
import bisq.user.identity.UserIdentityService;
import bisq.user.profile.UserProfile;
import bisq.user.profile.UserProfileService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Getter
public class ChatService implements Service {
    private final BisqEasyPrivateTradeChatChannelService bisqEasyPrivateTradeChatChannelService;
    private final TwoPartyPrivateChatChannelService privateDiscussionChannelService;
    private final BisqEasyPublicChatChannelService bisqEasyPublicChatChannelService;
    private final CommonPublicChatChannelService publicDiscussionChannelService;
    private final BisqEasyChatChannelSelectionService bisqEasyChatChannelSelectionService;
    private final ChatChannelSelectionService discussionChatChannelSelectionService;
    private final TwoPartyPrivateChatChannelService privateSupportChannelService;
    private final CommonPublicChatChannelService publicSupportChannelService;
    private final ChatChannelSelectionService supportChatChannelSelectionService;
    private final TwoPartyPrivateChatChannelService privateEventsChannelService;
    private final CommonPublicChatChannelService publicEventsChannelService;
    private final ChatChannelSelectionService eventsChatChannelSelectionService;

    public ChatService(PersistenceService persistenceService,
                       ProofOfWorkService proofOfWorkService,
                       NetworkService networkService,
                       UserIdentityService userIdentityService,
                       UserProfileService userProfileService) {

        // Trade
        bisqEasyPrivateTradeChatChannelService = new BisqEasyPrivateTradeChatChannelService(persistenceService,
                networkService,
                userIdentityService,
                userProfileService,
                proofOfWorkService);
        bisqEasyPublicChatChannelService = new BisqEasyPublicChatChannelService(persistenceService,
                networkService,
                userIdentityService,
                userProfileService);
        bisqEasyChatChannelSelectionService = new BisqEasyChatChannelSelectionService(persistenceService,
                bisqEasyPrivateTradeChatChannelService,
                bisqEasyPublicChatChannelService);

        // Discussion
        privateDiscussionChannelService = new TwoPartyPrivateChatChannelService(persistenceService,
                networkService,
                userIdentityService,
                userProfileService,
                proofOfWorkService,
                ChatChannelDomain.DISCUSSION);
        publicDiscussionChannelService = new CommonPublicChatChannelService(persistenceService,
                networkService,
                userIdentityService,
                userProfileService,
                ChatChannelDomain.DISCUSSION,
                List.of(new CommonPublicChatChannel(ChatChannelDomain.DISCUSSION, "bisq"),
                        new CommonPublicChatChannel(ChatChannelDomain.DISCUSSION, "bitcoin"),
                        new CommonPublicChatChannel(ChatChannelDomain.DISCUSSION, "markets"),
                        new CommonPublicChatChannel(ChatChannelDomain.DISCUSSION, "economy"),
                        new CommonPublicChatChannel(ChatChannelDomain.DISCUSSION, "offTopic")));

        discussionChatChannelSelectionService = new ChatChannelSelectionService(persistenceService,
                privateDiscussionChannelService,
                publicDiscussionChannelService,
                ChatChannelDomain.DISCUSSION);

        // Events
        privateEventsChannelService = new TwoPartyPrivateChatChannelService(persistenceService,
                networkService,
                userIdentityService,
                userProfileService,
                proofOfWorkService,
                ChatChannelDomain.EVENTS);
        publicEventsChannelService = new CommonPublicChatChannelService(persistenceService,
                networkService,
                userIdentityService,
                userProfileService,
                ChatChannelDomain.EVENTS,
                List.of(new CommonPublicChatChannel(ChatChannelDomain.EVENTS, "conferences"),
                        new CommonPublicChatChannel(ChatChannelDomain.EVENTS, "meetups"),
                        new CommonPublicChatChannel(ChatChannelDomain.EVENTS, "podcasts"),
                        new CommonPublicChatChannel(ChatChannelDomain.EVENTS, "noKyc"),
                        new CommonPublicChatChannel(ChatChannelDomain.EVENTS, "nodes"),
                        new CommonPublicChatChannel(ChatChannelDomain.EVENTS, "tradeEvents")));
        eventsChatChannelSelectionService = new ChatChannelSelectionService(persistenceService,
                privateEventsChannelService,
                publicEventsChannelService,
                ChatChannelDomain.EVENTS);

        // Support
        privateSupportChannelService = new TwoPartyPrivateChatChannelService(persistenceService,
                networkService,
                userIdentityService,
                userProfileService,
                proofOfWorkService,
                ChatChannelDomain.SUPPORT);
        publicSupportChannelService = new CommonPublicChatChannelService(persistenceService,
                networkService,
                userIdentityService,
                userProfileService,
                ChatChannelDomain.SUPPORT,
                List.of(new CommonPublicChatChannel(ChatChannelDomain.SUPPORT, "support"),
                        new CommonPublicChatChannel(ChatChannelDomain.SUPPORT, "questions"),
                        new CommonPublicChatChannel(ChatChannelDomain.SUPPORT, "reports")));
        supportChatChannelSelectionService = new ChatChannelSelectionService(persistenceService,
                privateSupportChannelService,
                publicSupportChannelService,
                ChatChannelDomain.SUPPORT);
    }

    @Override
    public CompletableFuture<Boolean> initialize() {
        log.info("initialize");
        return CompletableFutureUtils.allOf(
                bisqEasyPrivateTradeChatChannelService.initialize(),
                bisqEasyPublicChatChannelService.initialize(),
                bisqEasyChatChannelSelectionService.initialize(),

                privateDiscussionChannelService.initialize(),
                publicDiscussionChannelService.initialize(),
                discussionChatChannelSelectionService.initialize(),

                privateEventsChannelService.initialize(),
                publicEventsChannelService.initialize(),
                eventsChatChannelSelectionService.initialize(),

                privateSupportChannelService.initialize(),
                publicSupportChannelService.initialize(),
                supportChatChannelSelectionService.initialize()
        ).thenApply(list -> true);
    }

    @Override
    public CompletableFuture<Boolean> shutdown() {
        log.info("shutdown");
        return CompletableFutureUtils.allOf(
                bisqEasyPrivateTradeChatChannelService.shutdown(),
                bisqEasyPublicChatChannelService.shutdown(),
                bisqEasyChatChannelSelectionService.shutdown(),

                privateDiscussionChannelService.shutdown(),
                publicDiscussionChannelService.shutdown(),
                discussionChatChannelSelectionService.shutdown(),

                privateEventsChannelService.shutdown(),
                publicEventsChannelService.shutdown(),
                eventsChatChannelSelectionService.shutdown(),

                privateSupportChannelService.shutdown(),
                publicSupportChannelService.shutdown(),
                supportChatChannelSelectionService.shutdown()

        ).thenApply(list -> true);
    }

    public ChatChannelService<? extends ChatMessage,
            ? extends ChatChannel<? extends ChatMessage>,
            ? extends PersistableStore<?>> getChatChannelService(ChatChannel<?> chatChannel) {
        boolean isPrivateChatChannel = chatChannel instanceof PrivateChatChannel;
        switch (chatChannel.getChatChannelDomain()) {
            case BISQ_EASY:
                if (isPrivateChatChannel) {
                    return bisqEasyPrivateTradeChatChannelService;
                } else {
                    return bisqEasyPublicChatChannelService;
                }
            case DISCUSSION:
                if (isPrivateChatChannel) {
                    return privateDiscussionChannelService;
                } else {
                    return publicDiscussionChannelService;
                }
            case EVENTS:
                if (isPrivateChatChannel) {
                    return privateEventsChannelService;
                } else {
                    return publicEventsChannelService;
                }
            case SUPPORT:
                if (isPrivateChatChannel) {
                    return privateSupportChannelService;
                } else {
                    return publicSupportChannelService;
                }
            default:
                throw new RuntimeException("Unexpected chatChannelDomain");
        }
    }

    public void reportUserProfile(UserProfile userProfile, String reason) {
        //todo report user to admin and moderators, add reason
        log.info("called reportChatUser {} {}", userProfile, reason);
    }
}