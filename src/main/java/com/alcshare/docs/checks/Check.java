package com.alcshare.docs.checks;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Check
{
    @NotNull List<? extends Result> performCheck();

    public interface Result
    {
        @NotNull String getText();
    }
}
