package com.horizen.examples;

import com.horizen.block.SidechainBlock;
import com.horizen.box.Box;
import com.horizen.proposition.Proposition;
import com.horizen.state.ApplicationState;
import com.horizen.state.SidechainStateReader;
import scala.util.Success;
import scala.util.Try;

import java.util.List;

public class DefaultApplicationState implements ApplicationState {
    @Override
    public boolean validate(SidechainStateReader stateReader, SidechainBlock block) {
        return true;
    }

    @Override
    public Try<ApplicationState> onApplyChanges(SidechainStateReader stateReader, byte[] version, List<Box<Proposition>> newBoxes, List<byte[]> boxIdsToRemove) {
        return new Success<>(this);
    }

    @Override
    public Try<ApplicationState> onRollback(byte[] version) {
        return new Success<>(this);
    }
}
