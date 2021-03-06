[#import "difftable.ftl" as difftable]

[#macro diffbox diffModel index]

<div class="diff box" data-commit="${commit.getCommit()}" data-old-path="${diffModel.oldPath}" data-new-path="${diffModel.newPath}">
    <div class="header">
        <button class="pull-right btn btn-sm btn-default folder"><i class="glyphicon glyphicon-chevron-up"></i> ${i18n.translate("button.label.fold")}</button>
        <button class="pull-right btn btn-sm btn-default unfolder" style="display: none;"><i class="glyphicon glyphicon-chevron-down"></i> ${i18n.translate("button.label.unfold")}</button>
        [#if diffModel.isDeleted()]
            <h5><span class="label label-danger">Deleted</span> ${diffModel.oldPath}</h5>
        [#else]
            <a href="/courses/${group.course.code}/groups/${group.groupNumber}/commits/${commit.commit}/raw/${diffModel.newPath?url('UTF8')}" class="pull-right btn btn-sm btn-default" style="margin-right:5px;"><i class="glyphicon glyphicon-floppy-save"></i> ${i18n.translate("button.label.download")}</a>
            [#if diffModel.isMoved()]
                <h5><span class="label label-warn">${i18n.translate("diff.type.moved")}</span> ${diffModel.oldPath} -&gt; ${diffModel.newPath}</h5>
            [#elseif diffModel.isCopied()]
                <h5><span class="label label-warn">${i18n.translate("diff.type.copied")}</span> ${diffModel.oldPath} -&gt; ${diffModel.newPath}</h5>
            [#elseif diffModel.isAdded()]
                <h5><span class="label label-success">${i18n.translate("diff.type.created")}</span> </i> ${diffModel.newPath}</h5>
            [#elseif diffModel.isModified()]
                <h5><span class="label label-primary">${i18n.translate("diff.type.modified")}</span> ${diffModel.newPath}</h5>
            [/#if]
        [/#if]
    </div>
    [#if  diffModel.contexts?has_content]
        <div class="overflow-hidden">
            [@difftable.diffTable diffModel index commit/]
        </div>
    [/#if]
</div>
[/#macro]

[#macro renderScripts]
<script>
    $(document).ready(function() {
        $(".diff").each(function () {
            var diffBody = $(this).find(".diffs");
            if (diffBody.length == 0) {
                var folder = $(this).find(".folder");
                folder.css("display", "none");
            }
        });

        $(".folder").click(function (e) {
            var body = $(this).parentsUntil(".box").parent();
            var unfolder = $(this).parent().find(".unfolder");

            body.addClass("folded");
            $(this).css("display", "none").blur();
            unfolder.css("display", "block");
        });
        $(".unfolder").click(function (e) {
            var body = $(this).parentsUntil(".box").parent();
            var folder = $(this).parent().find(".folder");

            body.removeClass("folded");
            $(this).css("display", "none").blur();
            folder.css("display", "block");
        });
    });
</script>
[/#macro]