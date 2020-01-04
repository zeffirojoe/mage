package mage.cards.e;

import mage.abilities.Ability;
import mage.abilities.Mode;
import mage.abilities.dynamicvalue.common.ManacostVariableValue;
import mage.abilities.effects.common.ExileTargetEffect;
import mage.abilities.effects.common.GainLifeEffect;
import mage.abilities.effects.common.continuous.BoostTargetEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Duration;
import mage.filter.StaticFilters;
import mage.game.Game;
import mage.target.common.TargetCardInGraveyard;
import mage.target.common.TargetCreaturePermanent;
import mage.target.targetadjustment.TargetAdjuster;

import java.util.UUID;

/**
 * @author TheElk801
 */
public final class ErebossIntervention extends CardImpl {

    public ErebossIntervention(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.INSTANT}, "{X}{B}");

        // Choose one —
        // • Target creature gets -X/-X until end of turn. You gain X life.
        this.getSpellAbility().addEffect(new BoostTargetEffect(
                ManacostVariableValue.instance, ManacostVariableValue.instance, Duration.EndOfTurn
        ));
        this.getSpellAbility().addEffect(new GainLifeEffect(ManacostVariableValue.instance)
                .setText("You gain X life"));
        this.getSpellAbility().addTarget(new TargetCreaturePermanent());

        // • Exile up to twice X target cards from graveyards.
        this.getSpellAbility().addMode(new Mode(new ExileTargetEffect()
                .setText("Exile up to twice X target cards from graveyards.")));
        this.getSpellAbility().setTargetAdjuster(ErebossInterventionAdjuster.instance);
    }

    private ErebossIntervention(final ErebossIntervention card) {
        super(card);
    }

    @Override
    public ErebossIntervention copy() {
        return new ErebossIntervention(this);
    }
}

enum ErebossInterventionAdjuster implements TargetAdjuster {
    instance;

    @Override
    public void adjustTargets(Ability ability, Game game) {
        Mode mode = ability.getModes().getMode();
        if (mode.getEffects().stream().noneMatch(ExileTargetEffect.class::isInstance)) {
            return;
        }
        mode.getTargets().clear();
        mode.addTarget(new TargetCardInGraveyard(
                0, 2 * ability.getManaCostsToPay().getX(), StaticFilters.FILTER_CARD_CARDS
        ));
    }
}