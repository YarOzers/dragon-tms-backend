const testCase = [
    {
        "id": 1,
        "name": "Название",
        "type": "TESTCASE",
        "automationFlag": "AUTO",
        "folder": {
            "id": 1,
            "name": "123",
            "childFolders": [],
            "testCases": [
                1
            ],
            "type": "FOLDER",
            "testPlan": null,
            "parentFolderId": null
        },
        "user": {
            "id": 1,
            "name": "Yaroslav",
            "role": "ADMIN",
            "rights": "SUPER",
            "projects": [
                {
                    "id": 1,
                    "name": "123",
                    "users": [],
                    "folders": [
                        {
                            "id": 1,
                            "name": "123",
                            "childFolders": [],
                            "testCases": [
                                1
                            ],
                            "type": "FOLDER",
                            "testPlan": null,
                            "parentFolderId": null
                        }
                    ],
                    "testPlans": [
                        {
                            "id": 1,
                            "name": "Тест план 1",
                            "createdDate": "2024-08-19T14:05:28",
                            "user": {
                                "id": 1,
                                "name": "Yaroslav",
                                "role": "ADMIN",
                                "rights": "SUPER",
                                "projects": [
                                    1
                                ],
                                "testPlans": [
                                    1
                                ],
                                "testCases": [
                                    1
                                ],
                                "testCaseResults": [
                                    {
                                        "id": 1,
                                        "user": {
                                            "id": 1,
                                            "name": "Yaroslav",
                                            "role": "ADMIN",
                                            "rights": "SUPER",
                                            "projects": [
                                                1
                                            ],
                                            "testPlans": [
                                                1
                                            ],
                                            "testCases": [
                                                1
                                            ],
                                            "testCaseResults": [
                                                1
                                            ]
                                        },
                                        "executedTime": "2024-08-19T14:07:14",
                                        "result": null,
                                        "testPlanId": 1,
                                        "testCase": 1
                                    }
                                ]
                            },
                            "startDate": "2024-08-18T14:05:40",
                            "finishDate": "2024-08-19T14:05:31",
                            "testCaseCount": 1,
                            "status": "FINISHED",
                            "qas": [],
                            "folders": [],
                            "project": 1
                        }
                    ],
                    "createdDate": "2024-08-19T03:12:30.065753"
                }
            ],
            "testPlans": [
                1
            ],
            "testCases": [
                1
            ],
            "testCaseResults": [
                1
            ]
        },
        "data": [
            {
                "id": 1,
                "automationFlag": "AUTO",
                "changesAuthor": {
                    "id": 1,
                    "name": "Yaroslav",
                    "role": "ADMIN",
                    "rights": "SUPER",
                    "projects": [
                        1
                    ],
                    "testPlans": [
                        1
                    ],
                    "testCases": [
                        1
                    ],
                    "testCaseResults": [
                        1
                    ]
                },
                "createdDate": "2024-08-19T13:53:46",
                "executionTime": "2024-08-19T13:53:53",
                "expectedExecutionTime": "2024-08-19T13:53:57",
                "name": "Новое имя тест-кейса",
                "preConditionItems": [
                    {
                        "id": 1,
                        "selected": false,
                        "action": "Действие",
                        "expectedResult": "Результат",
                        "testCaseData": 1
                    }
                ],
                "stepItems": [
                    {
                        "id": 1,
                        "selected": false,
                        "action": "Действие",
                        "expectedResult": "Ожидаемый результат",
                        "testCaseData": 1
                    }
                ],
                "postConditionItems": [
                    {
                        "id": 1,
                        "selected": false,
                        "action": "Действие",
                        "expectedResult": "Ожидаемый результат",
                        "testCaseData": 1
                    }
                ],
                "priority": "HIGHEST",
                "type": "FUNCTIONAL",
                "version": 1,
                "status": "READY",
                "testCase": 1
            }
        ],
        "lastDataIndex": 0,
        "loading": false,
        "results": [
            1
        ],
        "selected": false,
        "running": false,
        "new": true
    }
]