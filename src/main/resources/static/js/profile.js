$(document).ready(getAllSkillsByToken());

function getAllSkillsByToken() {
    var url_string = window.location.href;
    var url = new URL(url_string);
    var token = url.searchParams.get("thd_i8");
    $.ajax({
        method: "GET",
        url: gOptions.aws_path + "/candidateskill/getsortedskills/" + token,
        success: function (response) {
            for (let i = 0; i < response.length; i++) {
                if (response[i][0] != null && response[i][0].typeStr === "Tool") {
                    fillToolSkillsTable(response[i]);
                    continue;
                }
                if (response[i][0] != null && response[i][0].typeStr === "Soft") {
                    fillSoftSkillTable(response[i]);
                    continue;
                }
                if (response[i][0] != null && response[i][0].typeStr === "Technical") {
                    fillTechnicalSkillsTable(response[i]);
                    continue;
                }
                if (response[i][0] != null && response[i][0].typeStr === "Language") {
                    console.log(response[i]);
                    fillLanguageSkillTable(response[i]);
                    break;
                }
            }
            getAllCandidatesSkills();
        }
    });
}

function updateSkills() {
    var url_string = window.location.href;
    var url = new URL(url_string);
    var token = url.searchParams.get("thd_i8");
    $.ajax({
        method: "PUT",
        url: gOptions.aws_path + "/candidateskill/saveCandidateSkills/" + token,
        data: JSON.stringify(prepareData()),
        contentType: "application/json",
        success: function () {
        }
    });
}

function fillTechnicalSkillsTable(technicalSkills) {
    let tbody = "";
    for (let i = 0; i < technicalSkills.length; i++) {
        tbody += "<tr>";
        tbody += `<td hidden id="technicalSkill${i}">${technicalSkills[i].id}</td>`;
        tbody += "<td>" + (i + 1) + "</td>";
        tbody += "<td>" + technicalSkills[i].name + "</td>";
        tbody += `<td><input type="radio" id="never-used-id${i}" name="technicalGroup[${i}]" value="Never Used"/></td>`;
        tbody += `<td><input type="radio" id="elementary-id${i}" name="technicalGroup[${i}]" value="Elementary"/></td>`;
        tbody += `<td><input type="radio" id="middle-id${i}" name="technicalGroup[${i}]" value="Middle"/></td>`;
        tbody += `<td><input type="radio" id="upper-id${i}" name="technicalGroup[${i}]" value="Upper"/></td>`;
        tbody += `<td><input type="radio" id="advanced-id${i}" name="technicalGroup[${i}]" value="Advanced"/></td>`;
        tbody += "</tr>";
    }
    $("#tbodyForTechnicalSkills").html(tbody);
}

function fillToolSkillsTable(toolSkills) {
    let tbody = "";
    for (let i = 0; i < toolSkills.length; i++) {
        tbody += "<tr>";
        tbody += `<td hidden id="toolSkill${i}">${toolSkills[i].id}</td>`;
        tbody += "<td>" + (i + 1) + "</td>";
        tbody += "<td>" + toolSkills[i].name + "</td>";
        tbody += `<td><input type="radio" id="tool-never-used-id${i}" name="toolGroup[${i}]" value="Never Used"/></td>`;
        tbody += `<td><input type="radio" id="tool-elementary-id${i}" name="toolGroup[${i}]" value="Elementary"/></td>`;
        tbody += `<td><input type="radio" id="tool-middle-id${i}" name="toolGroup[${i}]" value="Middle"/></td>`;
        tbody += `<td><input type="radio" id="tool-upper-id${i}" name="toolGroup[${i}]" value="Upper"/></td>`;
        tbody += `<td><input type="radio" id="tool-advanced-id${i}" name="toolGroup[${i}]" value="Advanced"/></td>`;
        tbody += "</tr>";
    }
    $("#tbodyForToolSkills").html(tbody);
}

function fillSoftSkillTable(softSkills) {
    let tbody = "";
    for (let i = 0; i < softSkills.length; i++) {
        tbody += "<tr>";
        tbody += `<td hidden id="softSkill${i}">${softSkills[i].id}</td>`;
        tbody += `<td>${i + 1}</td>`;
        tbody += "<td>" + softSkills[i].name + "</td>";
        tbody += `<td><input type="radio" id="very-rarely-id${i}" name="softGroup[${i}]" value="Very rarely"/></td>`;
        tbody += `<td><input type="radio" id="rarely-id${i}" name="softGroup[${i}]" value="Rarely"/></td>`;
        tbody += `<td><input type="radio" id="sometimes-id${i}" name="softGroup[${i}]" value="SomeTimes"/></td>`;
        tbody += `<td><input type="radio" id="often-id${i}" name="softGroup[${i}]" value="Often"/></td>`;
        tbody += `<td><input type="radio" id="very-often-id${i}" name="softGroup[${i}]" value="Very Often"/></td>`;
        tbody += "</tr>";
    }
    $("#tbodyForSoftSkills").html(tbody);
}

function fillLanguageSkillTable(languageSkill) {
    console.log(languageSkill);
    let tbody = "";
    for (let i = 0; i < languageSkill.length; i++) {
        tbody += `<tr>`;
        tbody += `<td hidden id="englishSkillId${i}">${languageSkill[i].id}</td>`;
        tbody += `<td>${i + 1}</td>`;
        tbody += `<td>` + languageSkill[i].name + `</td>`;
        tbody += `<td><input type="radio" id="a1-level-id${i}" name="englishGroup[${i}]" value="A1"/></td>`;
        tbody += `<td><input type="radio" id="a2-level-id${i}" name="englishGroup[${i}]" value="A2"/></td>`;
        tbody += `<td><input type="radio" id="b1-level-id${i}" name="englishGroup[${i}]" value="B1"/></td>`;
        tbody += `<td><input type="radio" id="b2-level-id${i}" name="englishGroup[${i}]" value="B2"/></td>`;
        tbody += `<td><input type="radio" id="c1-level-id${i}" name="englishGroup[${i}]" value="C1"/></td>`;
        tbody += `<td><input type="radio" id="c2-level-id${i}" name="englishGroup[${i}]" value="C2"/></td>`;
        tbody += `</tr>`;
    }
    $("#tbodyForLanguageSkill").html(tbody);
}

function prepareData() {
    let returnList = [];

    for (let i = 0; i < $('#technicalSkillTable tr').length - 1; i++) {
        if ($("#never-used-id" + i).is(":checked") === true) {
            returnList.push(createObject($("#technicalSkill" + i).text(), $("#never-used-id" + i).val()))
        }
        if ($("#elementary-id" + i).is(":checked") === true) {
            returnList.push(createObject($("#technicalSkill" + i).text(), $("#elementary-id" + i).val()))
        }
        if ($("#middle-id" + i).is(":checked") === true) {
            returnList.push(createObject($("#technicalSkill" + i).text(), $("#middle-id" + i).val()))
        }
        if ($("#upper-id" + i).is(":checked") === true) {
            returnList.push(createObject($("#technicalSkill" + i).text(), $("#upper-id" + i).val()))
        }
        if ($("#advanced-id" + i).is(":checked") === true) {
            returnList.push(createObject($("#technicalSkill" + i).text(), $("#advanced-id" + i).val()))
        }
    }

    for (let i = 0; i < $('#sofSkillTable tr').length - 1; i++) {
        if ($("#very-rarely-id" + i).is(":checked") === true) {
            returnList.push(createObject($("#softSkill" + i).text(), $("#very-rarely-id" + i).val()))
        }
        if ($("#rarely-id" + i).is(":checked") === true) {
            returnList.push(createObject($("#softSkill" + i).text(), $("#rarely-id" + i).val()))
        }
        if ($("#sometimes-id" + i).is(":checked") === true) {
            returnList.push(createObject($("#softSkill" + i).text(), $("#sometimes-id" + i).val()))
        }
        if ($("#often-id" + i).is(":checked") === true) {
            returnList.push(createObject($("#softSkill" + i).text(), $("#often-id" + i).val()))
        }
        if ($("#very-often-id" + i).is(":checked") === true) {
            returnList.push(createObject($("#softSkill" + i).text(), $("#very-often-id" + i).val()))
        }
    }

    for (let i = 0; i < $('#toolSkillTable tr').length - 1; i++) {
        if ($("#tool-never-used-id" + i).is(":checked") === true) {
            returnList.push(createObject($("#toolSkill" + i).text(), $("#tool-never-used-id" + i).val()))
        }
        if ($("#tool-elementary-id" + i).is(":checked") === true) {
            returnList.push(createObject($("#toolSkill" + i).text(), $("#tool-elementary-id" + i).val()))
        }
        if ($("#tool-middle-id" + i).is(":checked") === true) {
            returnList.push(createObject($("#toolSkill" + i).text(), $("#tool-middle-id" + i).val()))
        }
        if ($("#tool-upper-id" + i).is(":checked") === true) {
            returnList.push(createObject($("#toolSkill" + i).text(), $("#tool-upper-id" + i).val()))
        }
        if ($("#tool-advanced-id" + i).is(":checked") === true) {
            returnList.push(createObject($("#toolSkill" + i).text(), $("#tool-advanced-id" + i).val()))
        }
    }

    for (let i = 0; i < $('#englishLevelTable tr').length; i++) {
        if ($("#a1-level-id" + i).is(":checked") === true) {
            returnList.push(createObject($("#englishSkillId" + i).text(), $("#a1-level-id" + i).val()));
        }
        if ($('#a2-level-id' + i).is(":checked") === true) {
            returnList.push(createObject($("#englishSkillId" + i).text(), $('#a2-level-id' + i).val()));
        }
        if ($('#b1-level-id' + i).is(":checked") === true) {
            returnList.push(createObject($("#englishSkillId" + i).text(), $('#b1-level-id' + i).val()));
        }
        if ($('#b2-level-id' + i).is(":checked") === true) {
            returnList.push(createObject($("#englishSkillId" + i).text(), $('#b2-level-id' + i).val()));
        }
        if ($('#c1-level-id' + i).is(":checked") === true) {
            returnList.push(createObject($("#englishSkillId" + i).text(), $('#c1-level-id' + i).val()));
        }
        if ($('#c2-level-id' + i).is(":checked") === true) {
            returnList.push(createObject($("#englishSkillId" + i).text(), $('#c2-level-id' + i).val()));
        }
    }

    return returnList;
}

function createObject(skillId, value) {
    return {
        skillId: skillId,
        level: value
    }
}

function getAllCandidatesSkills() {
    var url_string = window.location.href;
    var url = new URL(url_string);
    var token = url.searchParams.get("thd_i8");
    $.ajax({
        method: "GET",
        url: gOptions.aws_path + "/candidateskill/findallcandidateskills/" + token,
        success: function (response) {
            fillSelectedSkills(response);
        }
    });
}

function fillSelectedSkills(data) {
    for (let i = 0; i < data.length; i++) {
        for (let j = 0; j < $('#technicalSkillTable tr').length - 1; j++) {
            if ($("#technicalSkill" + j).text() === data[i].skillId + '') {
                if ($("#never-used-id" + j).val() === data[i].level) {
                    $("#never-used-id" + j).attr("checked", true);
                }
                if ($("#elementary-id" + j).val() === data[i].level) {
                    $("#elementary-id" + j).attr("checked", true);
                }
                if ($("#middle-id" + j).val() === data[i].level) {
                    $("#middle-id" + j).attr("checked", true);
                }
                if ($("#upper-id" + j).val() === data[i].level) {
                    $("#upper-id" + j).attr("checked", true);
                }
                if ($("#advanced-id" + j).val() === data[i].level) {
                    $("#advanced-id" + j).attr("checked", true);
                }
            }
        }

        for (let j = 0; j < $('#sofSkillTable tr').length - 1; j++) {
            if ($("#softSkill" + j).text() === data[i].skillId + '') {
                if ($("#very-rarely-id" + j).val() === data[i].level) {
                    $("#very-rarely-id" + j).attr("checked", true);
                }
                if ($("#rarely-id" + j).val() === data[i].level) {
                    $("#rarely-id" + j).attr("checked", true);
                }
                if ($("#sometimes-id" + j).val() === data[i].level) {
                    $("#sometimes-id" + j).attr("checked", true);
                }
                if ($("#often-id" + j).val() === data[i].level) {
                    $("#often-id" + j).attr("checked", true);
                }
                if ($("#very-often-id" + j).val() === data[i].level) {
                    $("#very-often-id" + j).attr("checked", true);
                }
            }
        }

        for (let j = 0; j < $('#toolSkillTable tr').length - 1; j++) {
            if ($("#toolSkill" + j).text() === data[i].skillId + '') {
                if ($("#tool-never-used-id" + j).val() === data[i].level) {
                    $("#never-used-id" + j).attr("checked", true);
                }
                if ($("#tool-elementary-id" + j).val() === data[i].level) {
                    $("#tool-elementary-id" + j).attr("checked", true);
                }
                if ($("#tool-middle-id" + j).val() === data[i].level) {
                    $("#tool-middle-id" + j).attr("checked", true);
                }
                if ($("#tool-upper-id" + j).val() === data[i].level) {
                    $("#tool-upper-id" + j).attr("checked", true);
                }
                if ($("#tool-advanced-id" + j).val() === data[i].level) {
                    $("#tool-advanced-id" + j).attr("checked", true);
                }
            }
        }

        for (let j = 0; j < $("#englishLevelTable tr").length; j++) {
            if ($("#englishSkillId" + j).text() === data[i].skillId + '') {
                if ($("#a1-level-id" + j).val() === data[i].level) {
                    $("#a1-level-id" + j).attr("checked", true);
                }
                if ($("#a2-level-id" + j).val() === data[i].level) {
                    $("#a2-level-id" + j).attr("checked", true);
                }
                if ($("#b1-level-id" + j).val() === data[i].level) {
                    $("#b1-level-id" + j).attr("checked", true);
                }
                if ($("#b2-level-id" + j).val() === data[i].level) {
                    $("#b2-level-id" + j).attr("checked", true);
                }
                if ($("#c1-level-id" + j).val() === data[i].level) {
                    $("#c1-level-id" + j).attr("checked", true);
                }
                if ($("#c2-level-id" + j).val() === data[i].level) {
                    $("#c2-level-id" + j).attr("checked", true);
                }
            }
        }
    }
}