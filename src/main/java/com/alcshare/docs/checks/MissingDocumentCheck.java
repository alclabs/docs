package com.alcshare.docs.checks;

import com.alcshare.docs.DocumentManager;
import com.alcshare.docs.DocumentReference;
import com.alcshare.docs.util.AddOnFiles;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MissingDocumentCheck implements Check
{
    @Override @NotNull public List<Result> performCheck()
    {
        List<DocumentReference> allReferences = DocumentManager.INSTANCE.getAllReferences();
        return performCheckOnList(allReferences);
    }

    @NotNull private List<Result> performCheckOnList(@NotNull List<DocumentReference> allReferences)
    {
        List<Result> results = new ArrayList<Result>();
        for (DocumentReference reference : allReferences)
        {
            Result result = performCheckOnRef(reference);
            if (result != null)
                results.add(result);
        }
        return results;
    }

    @Nullable private Result performCheckOnRef(@NotNull DocumentReference reference)
    {
        if (reference.getPathType().equalsIgnoreCase("DOC"))
        {
            File file = canonicalize(new File(AddOnFiles.getDocDirectory(), reference.getDocPath()));
            if (!file.exists())
                return new Result(reference, "Missing document.  Referenced from \"%1$s\", file \"%2$s\" could not be found (looked for it at \"" + file.getAbsolutePath() + "\"");
        }
        return null;
    }

    private File canonicalize(File file)
    {
        try {
            return file.getCanonicalFile();
        } catch (IOException e) {
            return file;
        }
    }

    public class Result implements Check.Result
    {
        private String gqlPath;
        private String docPath;
        private String message;

        Result(String gqlPath, String docPath, String message)
        {
            this.gqlPath = gqlPath;
            this.docPath = docPath;
            this.message = message;
        }

        Result(DocumentReference reference, String message)
        {
            this(reference.getGqlPath(), reference.getDocPath(), message);
        }

        @NotNull @Override public String getText()
        {
            return String.format(message, gqlPath, docPath);
        }
    }
}