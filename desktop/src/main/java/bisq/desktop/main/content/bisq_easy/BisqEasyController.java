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

package bisq.desktop.main.content.bisq_easy;

import bisq.desktop.ServiceProvider;
import bisq.desktop.common.view.Controller;
import bisq.desktop.common.view.NavigationTarget;
import bisq.desktop.common.view.TabController;
import bisq.desktop.main.content.bisq_easy.offerbook.BisqEasyOfferbookController;
import bisq.desktop.main.content.bisq_easy.onboarding.BisqEasyOnboardingController;
import bisq.desktop.main.content.bisq_easy.open_trades.BisqEasyOpenTradesController;
import bisq.desktop.main.content.bisq_easy.private_chats.BisqEasyPrivateChatsController;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class BisqEasyController extends TabController<BisqEasyModel> {
    private final ServiceProvider serviceProvider;
    @Getter
    private final BisqEasyView view;

    public BisqEasyController(ServiceProvider serviceProvider) {
        super(new BisqEasyModel(), NavigationTarget.BISQ_EASY);

        this.serviceProvider = serviceProvider;

        view = new BisqEasyView(model, this);
    }

    @Override
    public void onActivate() {
    }

    @Override
    public void onDeactivate() {
    }

    @Override
    protected Optional<? extends Controller> createController(NavigationTarget navigationTarget) {
        switch (navigationTarget) {
            case BISQ_EASY_ONBOARDING: {
                return Optional.of(new BisqEasyOnboardingController(serviceProvider));
            }
            case BISQ_EASY_OFFERBOOK: {
                return Optional.of(new BisqEasyOfferbookController(serviceProvider));
            }
            case BISQ_EASY_OPEN_TRADES: {
                return Optional.of(new BisqEasyOpenTradesController(serviceProvider));
            }
            case BISQ_EASY_PRIVATE_CHAT: {
                return Optional.of(new BisqEasyPrivateChatsController(serviceProvider));
            }
            default: {
                return Optional.empty();
            }
        }
    }
}
