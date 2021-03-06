/*
 * Copyright (C) 2016 Álinson Santos Xavier <isoron@gmail.com>
 *
 * This file is part of Loop Habit Tracker.
 *
 * Loop Habit Tracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Loop Habit Tracker is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.isoron.uhabits.ui.habits.show;

import android.content.*;
import android.support.annotation.*;
import android.support.v7.widget.*;

import org.isoron.uhabits.*;
import org.isoron.uhabits.models.*;
import org.isoron.uhabits.ui.*;
import org.isoron.uhabits.ui.habits.show.views.*;
import org.isoron.uhabits.utils.*;

import butterknife.*;

import static org.isoron.uhabits.utils.InterfaceUtils.*;

public class ShowHabitRootView extends BaseRootView
    implements ModelObservable.Listener
{
    @NonNull
    private Habit habit;

    @BindView(R.id.frequencyCard)
    FrequencyCard frequencyCard;

    @BindView(R.id.streakCard)
    StreakCard streakCard;

    @BindView(R.id.subtitleCard)
    SubtitleCard subtitleCard;

    @BindView(R.id.overviewCard)
    OverviewCard overviewCard;

    @BindView(R.id.strengthCard)
    ScoreCard scoreCard;

    @BindView(R.id.historyCard)
    HistoryCard historyCard;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @NonNull
    private Controller controller;

    public ShowHabitRootView(@NonNull Context context,
                             @NonNull Habit habit)
    {
        super(context);
        this.habit = habit;

        addView(inflate(getContext(), R.layout.show_habit, null));
        ButterKnife.bind(this);

        controller = new Controller() {};

        initCards();
        initToolbar();
    }

    @Override
    public boolean getDisplayHomeAsUp()
    {
        return true;
    }

    @NonNull
    @Override
    public Toolbar getToolbar()
    {
        return toolbar;
    }

    public void setController(@NonNull Controller controller)
    {
        this.controller = controller;
        historyCard.setController(controller);
    }

    @Override
    public int getToolbarColor()
    {
        if (!getStyledBoolean(getContext(), R.attr.useHabitColorAsPrimary))
            return super.getToolbarColor();

        return ColorUtils.getColor(getContext(), habit.getColor());
    }

    @Override
    public void onModelChange()
    {
        controller.onToolbarChanged();
    }

    @Override
    protected void initToolbar()
    {
        super.initToolbar();
        toolbar.setTitle(habit.getName());
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        habit.getObservable().addListener(this);
    }

    @Override
    protected void onDetachedFromWindow()
    {
        habit.getObservable().removeListener(this);
        super.onDetachedFromWindow();
    }

    private void initCards()
    {
        subtitleCard.setHabit(habit);
        overviewCard.setHabit(habit);
        scoreCard.setHabit(habit);
        historyCard.setHabit(habit);
        streakCard.setHabit(habit);
        frequencyCard.setHabit(habit);
    }

    public interface Controller extends HistoryCard.Controller
    {
        default void onToolbarChanged(){}
    }
}
