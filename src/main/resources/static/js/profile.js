$(document).ready(getAllSkillsByToken());

function getAllSkillsByToken() {
    var url_string = window.location.href;
    var url = new URL(url_string);
    var token = url.searchParams.get("thd_i8");
    $.ajax({
        method: "GET",
        url: gOptions.aws_path+"/candidateskill/getsortedskills/" + token,
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
                    break;
                }
            }
        }
    });
}

function updateSkills() {
    $.ajax({
        method: "PUT",
        url: gOptions.aws_path + "/candidateskill/saveCandidateSkills",
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
        tbody += `<td><input type="radio" id="never-used-id${i}" name="toolGroup[${i}]" value="Never Used"/></td>`;
        tbody += `<td><input type="radio" id="elementary-id${i}" name="toolGroup[${i}]" value="Elementary"/></td>`;
        tbody += `<td><input type="radio" id="middle-id${i}" name="toolGroup[${i}]" value="Middle"/></td>`;
        tbody += `<td><input type="radio" id="upper-id${i}" name="toolGroup[${i}]" value="Upper"/></td>`;
        tbody += `<td><input type="radio" id="advanced-id${i}" name="toolGroup[${i}]" value="Advanced"/></td>`;
        tbody += "</tr>";
    }
    $("#tbodyForToolSkills").html(tbody);
}

function fillSoftSkillTable(softSkills) {
    let tbody = "";
    for (let i = 0; i < softSkills.length; i++) {
        tbody += "<tr>";
        tbody += `<td hidden id="softSkill${i}">${softSkills[i].id}</td>`;
        tbody += "<td>" + (i + 1) + "</td>";
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

    for (let i = 0; i < $('#toolSkillTable tr').length - 1; i++) {
        if ($("#never-used-id" + i).is(":checked") === true) {
            returnList.push(createObject($("#toolSkill" + i).text(), $("#never-used-id" + i).val()))
        }
        if ($("#elementary-id" + i).is(":checked") === true) {
            returnList.push(createObject($("#toolSkill" + i).text(), $("#elementary-id" + i).val()))
        }
        if ($("#middle-id" + i).is(":checked") === true) {
            returnList.push(createObject($("#toolSkill" + i).text(), $("#middle-id" + i).val()))
        }
        if ($("#upper-id" + i).is(":checked") === true) {
            returnList.push(createObject($("#toolSkill" + i).text(), $("#upper-id" + i).val()))
        }
        if ($("#advanced-id" + i).is(":checked") === true) {
            returnList.push(createObject($("#toolSkill" + i).text(), $("#advanced-id" + i).val()))
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
    console.log(returnList);

    return returnList;
}

function createObject(skillId, value) {
    return {
        skillId: skillId,
        level: value
    }
}